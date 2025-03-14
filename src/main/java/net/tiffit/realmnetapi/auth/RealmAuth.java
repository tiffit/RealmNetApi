package net.tiffit.realmnetapi.auth;

import lombok.SneakyThrows;
import net.tiffit.realmnetapi.auth.data.PlayerChar;
import net.tiffit.realmnetapi.auth.data.ServerInfo;
import net.tiffit.realmnetapi.util.HashUtils;
import okhttp3.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;

public class RealmAuth {

    private static final String GAME_PLATFORM = "Unity";
    private static final String GAME_NET = GAME_PLATFORM;

    private static final DocumentBuilder builder;
    static {
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private static final OkHttpClient client = new OkHttpClient();

    @SneakyThrows
    public static AccessToken authenticate(RotmgEnv env, String email, String password){
        String hash = HashUtils.hash(email);
        RequestBody requestBody = new FormBody.Builder()
                .add("guid", email)
                .add("password", password)
                .add("game_net", GAME_NET)
                .add("play_platform", GAME_PLATFORM)
                .add("clientToken", hash)
                .build();
        Request request = new Request.Builder().url(env.baseUrl + "account/verify").post(requestBody).header("User-Agent", "UnityPlayer/2021.3.5f1 (UnityWebRequest/1.0, libcurl/7.80.0-DEV)").build();
        ResponseBody response = client.newCall(request).execute().body();
        assert response != null;
        Document doc = builder.parse(response.byteStream());
        return createAccessToken(email, hash, doc);
    }

    @SneakyThrows
    public static AccessToken authenticate(RotmgEnv env, AccessToken token){
        RequestBody requestBody = new FormBody.Builder()
                .add("guid", token.getEmail())
                .add("clientToken", token.getClientHash())
                .add("accessToken", token.getToken())
                .add("game_net", GAME_NET)
                .add("play_platform", GAME_PLATFORM)
                .build();
        Request request = new Request.Builder().url(env.baseUrl + "account/verify").post(requestBody).header("User-Agent", "UnityPlayer/2021.3.5f1 (UnityWebRequest/1.0, libcurl/7.80.0-DEV)").build();
        ResponseBody response = client.newCall(request).execute().body();
        assert response != null;
        Document doc = builder.parse(response.byteStream());
        return createAccessToken(token.getEmail(), token.getClientHash(), doc);
    }

    private static AccessToken createAccessToken(String email, String clientHash, Document doc){
        doc.normalizeDocument();
        Element docElem = doc.getDocumentElement();
        String accessToken = docElem.getElementsByTagName("AccessToken").item(0).getTextContent();
        long accessTokenExpire = Long.parseLong(docElem.getElementsByTagName("AccessTokenTimestamp").item(0).getTextContent()) +
                Long.parseLong(docElem.getElementsByTagName("AccessTokenExpiration").item(0).getTextContent());
        long accountId = Long.parseLong(docElem.getElementsByTagName("AccountId").item(0).getTextContent());
        String ign = docElem.getElementsByTagName("Name").item(0).getTextContent();
        return new AccessToken(accessToken, email, accessTokenExpire, accountId, ign, clientHash);
    }

    @SneakyThrows
    public static boolean verifyAccessTokenClient(RotmgEnv env, AccessToken token){
        RequestBody requestBody = new FormBody.Builder()
                .add("clientToken", token.getClientHash())
                .add("accessToken", token.getToken())
                .add("game_net", GAME_NET)
                .add("play_platform", GAME_PLATFORM)
                .build();
        Request request = new Request.Builder().url(env.baseUrl + "account/verifyAccessTokenClient").post(requestBody).header("User-Agent", "UnityPlayer/2021.3.5f1 (UnityWebRequest/1.0, libcurl/7.80.0-DEV)").build();
        ResponseBody response = client.newCall(request).execute().body();
        assert response != null;
        Document doc = builder.parse(response.byteStream());
        doc.normalizeDocument();
        Element docElem = doc.getDocumentElement();
        return docElem.getTagName().equals("Success");
    }

    @SneakyThrows
    public static List<PlayerChar> charList(RotmgEnv env, AccessToken token){
        RequestBody requestBody = new FormBody.Builder()
                .add("do_login", "true")
                .add("accessToken", token.getToken())
                .add("game_net", GAME_NET)
                .add("play_platform", GAME_PLATFORM)
                .build();
        Request request = new Request.Builder().url(env.baseUrl + "char/list").post(requestBody).header("User-Agent", "UnityPlayer/2021.3.5f1 (UnityWebRequest/1.0, libcurl/7.80.0-DEV)").build();
        ResponseBody response = client.newCall(request).execute().body();
        assert response != null;
        Document doc = builder.parse(response.byteStream());
        doc.normalizeDocument();
        NodeList charsNodes = doc.getDocumentElement().getChildNodes();

        List<PlayerChar> chars = new LinkedList<>();
        HashMap<Integer, Integer> maxClassLevel = new HashMap<>();
        try {
            for (int i = 0; i < charsNodes.getLength(); i++) {
                Element charElem = (Element) charsNodes.item(i);
                if (charElem.getTagName().equals("Char")){
                    NodeList charNodes = charElem.getChildNodes();
                    int id = Integer.parseInt(charElem.getAttribute("id"));
                    int objectType = 0, level = 0;
                    int[] equipment = null;
                    for (int j = 0; j < charNodes.getLength(); j++) {
                        Element elem = (Element) charNodes.item(j);
                        switch (elem.getNodeName()) {
                            case "ObjectType" -> objectType = Integer.parseInt(elem.getTextContent());
                            case "Level" -> level = Integer.parseInt(elem.getTextContent());
                            case "Equipment" -> equipment = Arrays.stream(elem.getTextContent().split(",")).mapToInt(Integer::parseInt).toArray();
                        }
                    }
                    chars.add(new PlayerChar(id, objectType, level, equipment));
                }else if(charElem.getTagName().equals("MaxClassLevelList")){
                    NodeList maxClassLevels = charElem.getChildNodes();
                    for (int j = 0; j < maxClassLevels.getLength(); j++) {
                        Element elem = (Element) maxClassLevels.item(j);
                        int id = Integer.parseInt(elem.getAttribute("classType"));
                        int level = Integer.parseInt(elem.getAttribute("maxLevel"));
                        maxClassLevel.put(id, level);
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        token.setMaxClassLevel(maxClassLevel);
        return chars;
    }

    @SneakyThrows
    public static void appInit(RotmgEnv env, AccessToken token){
        RequestBody requestBody = new FormBody.Builder()
                .add("game_net", "rotmg")
                .add("accessToken", token.getToken())
                .add("game_net", GAME_NET)
                .add("play_platform", GAME_PLATFORM)
                .build();
        Request request = new Request.Builder().url(env.baseUrl + "app/init?platform=standalonewindows64&key=9KnJFxtTvLu2frXv").post(requestBody).header("User-Agent", "UnityPlayer/2021.3.5f1 (UnityWebRequest/1.0, libcurl/7.80.0-DEV)").build();
        ResponseBody response = client.newCall(request).execute().body();
        assert response != null;
        response.close();
        return;
    }

    @SneakyThrows
    public static List<ServerInfo> getServers(RotmgEnv env, AccessToken token){
        List<ServerInfo> servers = new ArrayList<>();
        RequestBody requestBody = new FormBody.Builder()
                .add("accessToken", token.getToken())
                .add("game_net", GAME_NET)
                .add("play_platform", GAME_PLATFORM)
                .build();
        Request request = new Request.Builder().url(env.baseUrl + "account/servers").post(requestBody).header("User-Agent", "UnityPlayer/2021.3.5f1 (UnityWebRequest/1.0, libcurl/7.80.0-DEV)").build();
        ResponseBody response = client.newCall(request).execute().body();
        assert response != null;
        Document doc = builder.parse(response.byteStream());
        doc.normalizeDocument();
        NodeList serversNodes = doc.getDocumentElement().getChildNodes();
        for(int i = 0; i < serversNodes.getLength(); i++){
            Element serverElem = (Element) serversNodes.item(i);
            NodeList serverNodes = serverElem.getChildNodes();
            String name = "N/A";
            String dns = "";
            float usage = 0;
            for(int j = 0; j < serverNodes.getLength(); j++){
                Element elem = (Element) serverNodes.item(j);
                switch (elem.getNodeName()) {
                    case "Name" -> name = elem.getTextContent();
                    case "Usage" -> usage = Float.parseFloat(elem.getTextContent());
                    case "DNS" -> dns = elem.getTextContent();
                }
            }
            ServerInfo info = new ServerInfo(name, dns, usage);
            servers.add(info);
        }
        servers.sort((o1, o2) -> o2.name().compareTo(o1.name()));
        return servers;
    }

}
