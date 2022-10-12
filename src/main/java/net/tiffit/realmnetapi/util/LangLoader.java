package net.tiffit.realmnetapi.util;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class LangLoader {

    private static HashMap<String, String> langMap = new HashMap<>();
    private static Gson gson = new GsonBuilder().setLenient().create();

    public static void load() throws IOException {
        langMap.clear();
        JsonObject obj = gson.fromJson(new FileReader("./assets/loc.json"), JsonObject.class);
        for (String s : obj.keySet()) {
            langMap.put(s, obj.get(s).getAsString());
        }
    }

    public static String format(String str){
        try {
            try(JsonReader reader = gson.newJsonReader(new StringReader(str))){
                str = str.replaceFirst(",(?=\\s*?[}\\]])", "");
                JsonObject obj = gson.fromJson(reader, JsonObject.class);
                String key;
                if(obj.has("k"))key = obj.get("k").getAsString();
                else if(obj.has("t"))key = obj.get("t").getAsString();
                else return str;
                String localized = langMap.getOrDefault(key, str);
                if(obj.has("k") && obj.has("t")) {
                    for (Map.Entry<String, JsonElement> entry : obj.getAsJsonObject("t").entrySet()) {
                        if (localized.toLowerCase().contains("{" + entry.getKey().toLowerCase() + "}")) {
                            localized = localized.replaceFirst("(?i)\\{"+entry.getKey()+"}", entry.getValue().getAsString());
                        }
                    }
                }
                return localized;
            }
        }catch(JsonSyntaxException ex){

        }catch(JsonIOException | IOException ex){
            System.err.println("Error formatting string: " + str);
            ex.printStackTrace();
        }
        return str;
    }

}
