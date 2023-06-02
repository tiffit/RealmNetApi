package net.tiffit.realmnetapi.assets.xml;

import lombok.EqualsAndHashCode;
import net.tiffit.realmnetapi.assets.spritesheet.SpriteLocation;

import java.io.Serializable;

@EqualsAndHashCode
public class Texture implements Serializable {
    public String file = "invisible";
    public int index = 0;
    public boolean animated = false;

    public SpriteLocation toSpriteLocation(){
        return new SpriteLocation(file, index);
    }
}
