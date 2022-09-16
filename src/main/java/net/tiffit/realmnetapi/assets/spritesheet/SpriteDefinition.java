package net.tiffit.realmnetapi.assets.spritesheet;

import java.io.Serializable;

public class SpriteDefinition implements Serializable {

    public int index;
    public int aId; //atlas ID
    public boolean isT; //true if invisible texture
    public SpriteRect position;
    public SpriteRect maskPosition;

}
