package net.tiffit.realmnetapi.assets.spritesheet;

import java.io.Serializable;

public class SheetDefinition implements Serializable {

    public String spriteSheetName;
    public int atlasId;
    public SpriteDefinition[] elements;

}
