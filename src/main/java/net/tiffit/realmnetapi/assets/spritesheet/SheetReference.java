package net.tiffit.realmnetapi.assets.spritesheet;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SheetReference {

    private static final HashMap<SpriteLocation, SpriteDefinition> SPRITE_MAP = new HashMap<>();
    private static final HashMap<SpriteLocation, AnimMap> ANIM_SPRITE_MAP = new HashMap<>();

    private static BufferedImage CHARACTER_MASKS;
    private static BufferedImage GROUND_TILES; //id 1
    private static BufferedImage CHARACTERS; //id 2
    private static BufferedImage MAP_OBJECTS; //id 4

    @SneakyThrows
    public static void Init(String filePath){
        System.out.println("Filling out maps...");
        Spritesheet sheet = Spritesheet.INSTANCE;
        for (SheetDefinition sheetDef : sheet.sprites) {
            for (SpriteDefinition sprite : sheetDef.elements) {
                SPRITE_MAP.put(new SpriteLocation(sheetDef.spriteSheetName, sprite.index), sprite);
            }
        }
        System.out.println("SheetDefinition finished");
        for (AnimSpriteDefinition sprite : sheet.animatedSprites) {
            SpriteLocation loc = new SpriteLocation(sprite.spriteSheetName, sprite.index);
            ANIM_SPRITE_MAP.putIfAbsent(loc, new AnimMap());
            ANIM_SPRITE_MAP.get(loc).addDefinition(sprite);
        }
        System.out.println("AnimSpriteDefinition loaded");
        System.out.println("Downloading atlases...");
        CHARACTERS = downloadImage(filePath, "characters");
        CHARACTER_MASKS = downloadImage(filePath, "characters_masks");
        GROUND_TILES = downloadImage(filePath, "groundTiles");
        MAP_OBJECTS = downloadImage(filePath, "mapObjects");
    }

    public static BufferedImage getSprite(SpriteLocation location){
        if(SPRITE_MAP.containsKey(location)){
            SpriteDefinition definition = SPRITE_MAP.get(location);
            return getSprite(definition);
        }
        return null;
    }

    public static SpriteDefinition getSpriteDefinition(SpriteLocation location){
        return SPRITE_MAP.getOrDefault(location, null);
    }

    public static BufferedImage getMask(SpriteDefinition definition){
        SpriteRect rect = definition.maskPosition;
        return CHARACTER_MASKS.getSubimage(rect.x, rect.y, rect.w, rect.h);
    }

    public static SpriteDefinition getAnimatedSpriteDefinition(SpriteLocation location){
        if(ANIM_SPRITE_MAP.containsKey(location)){
            AnimMap map = ANIM_SPRITE_MAP.get(location);
            List<AnimSpriteDefinition> list = map.getDefinition(AnimMap.ACTION_IDLE, AnimMap.DIRECTION_SIDE);
            if(list.size() > 0){
                return list.get(0).spriteData;
            }
        }
        return null;
    }

    public static BufferedImage getAnimatedSprite(SpriteLocation location){
        SpriteDefinition definition = getAnimatedSpriteDefinition(location);
        if(definition != null){
            return getSprite(definition);
        }
        return null;
    }

    public static AnimMap getAnimatedSprites(SpriteLocation location){
        return ANIM_SPRITE_MAP.get(location);
    }

    public static Set<SpriteLocation> getSpriteLocations(){
        return SPRITE_MAP.keySet();
    }

    public static Set<SpriteLocation> getAnimatedSpriteLocations(){
        return ANIM_SPRITE_MAP.keySet();
    }

    public static BufferedImage getSprite(SpriteDefinition definition){
        BufferedImage atlas = getAtlas(definition.aId);
        if(atlas != null) {
            SpriteRect rect = definition.position;
            return atlas.getSubimage(rect.x, rect.y, rect.w, rect.h);
        }
        return null;
    }

    private static BufferedImage getAtlas(int id){
        switch (id){
            case 1: return GROUND_TILES;
            case 2: return CHARACTERS;
            case 4: return MAP_OBJECTS;
            default: return null;
        }
    }

    private static BufferedImage downloadImage(String filePath, String name) throws IOException {
        InputStream stream = new FileInputStream(filePath + name + ".png");
        BufferedImage image = ImageIO.read(stream);
        stream.close();
        return image;
    }

}
