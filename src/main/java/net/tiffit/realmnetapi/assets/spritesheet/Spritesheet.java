package net.tiffit.realmnetapi.assets.spritesheet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Spritesheet {

    public static Spritesheet INSTANCE;

    public SheetDefinition[] sprites;
    public AnimSpriteDefinition[] animatedSprites;

    public static void LoadSpriteSheets(InputStream stream){
        try {
            Gson gson = new GsonBuilder().setLenient().create();
            INSTANCE = gson.fromJson(new InputStreamReader(stream), Spritesheet.class);
            stream.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
