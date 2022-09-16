package net.tiffit.realmnetapi.assets.xml;

import net.tiffit.realmnetapi.assets.spritesheet.SpriteLocation;

import java.io.Serializable;

public class Texture implements Serializable {
    public String file = "invisible";
    public int index = 0;
    public boolean animated = false;

    public SpriteLocation toSpriteLocation(){
        return new SpriteLocation(file, index);
    }
}
