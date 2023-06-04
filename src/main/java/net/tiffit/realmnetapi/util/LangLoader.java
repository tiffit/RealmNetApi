package net.tiffit.realmnetapi.util;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class LangLoader {

    private static HashMap<String, String> langMap = new HashMap<>();
    private static Gson gson = new GsonBuilder().setLenient().create();

    public static void load() throws IOException {
        load(new FileReader("./assets/loc.json"));
    }

    public static void load(Reader reader) throws IOException {
        langMap.clear();
        JsonObject obj = gson.fromJson(reader, JsonObject.class);
        for (String s : obj.keySet()) {
            langMap.put(s, obj.get(s).getAsString());
        }
    }

    public static String format(String str){
        try {
            str = str.replaceFirst(",(?=\\s*?[}\\]])", "");
            try(JsonReader reader = gson.newJsonReader(new StringReader(str))){
                JsonObject obj = gson.fromJson(reader, JsonObject.class);
                String key;
                if(obj.has("k"))key = obj.get("k").getAsString();
                else if(obj.has("t"))key = obj.get("t").getAsString();
                else return str;
                String localized = langMap.getOrDefault(key, str);
                if(obj.has("k") && obj.has("t")) {
                    for (Map.Entry<String, JsonElement> entry : obj.getAsJsonObject("t").entrySet()) {
                        if (localized.toLowerCase().contains("{" + entry.getKey().toLowerCase() + "}")) {
                            String value = entry.getValue().getAsString();
                            localized = localized.replaceFirst("(?i)\\{"+entry.getKey()+"}", langMap.getOrDefault(value, value));
                        }
                    }
                }
                return localized;
            }
        }catch(JsonSyntaxException ignored){

        }catch(JsonIOException | IOException ex){
            System.err.println("Error formatting string: " + str);
            ex.printStackTrace();
        }
        return str;
    }

}
