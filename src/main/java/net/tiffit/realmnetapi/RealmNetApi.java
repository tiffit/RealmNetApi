package net.tiffit.realmnetapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.tiffit.realmnetapi.auth.RotmgEnv;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

public class RealmNetApi {

    public static String CLIENT_TOKEN = "b6c7a712f40c79621ee51ea4ec907768c96866af";
    public static RotmgEnv ENV = RotmgEnv.PRODUCTION;

    private static HashMap<String, String> locMap = new HashMap<>();

    public static void main(String[] args){
        try {
            DataInputStream stream = new DataInputStream(new FileInputStream("./assets/res"));
            byte[] data = new byte[stream.available()];
            stream.readFully(data);
            String str = new String(data, StandardCharsets.UTF_8);
            int startIndex = str.indexOf("I2Languages") + "I2Languages".length() + 15;

            StringBuilder builder = null;
            String key = null;
            boolean hitCount = false;
            boolean hasEnded = false;
            while(!hasEnded){
                while((int)str.charAt(startIndex) == 0){
                    startIndex++;
                }
                char c = str.charAt(startIndex);
                if(builder == null){
                    if(!hitCount){
                        hitCount = true;
                    }else{
                        builder = new StringBuilder();
                    }
                }else if((int)c == 3){
                    String val = builder.toString();
                    if(val.equals("CaptchaError.lastAttempt")){
                        hasEnded = true;
                    }
                    if(key == null){
                        key = val;
                    }else{
                        locMap.put(key, val);
                        key = null;
                    }
                    builder = null;
                    hitCount = false;
                }
                if(builder != null){
                    builder.append(c);
                }
                startIndex++;
            }
            Gson gson = new GsonBuilder().create();
            Files.writeString(new File("./assets/loc.json").toPath(), gson.toJson(locMap));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}