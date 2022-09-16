package net.tiffit.realmnetapi.assets.spritesheet;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class AnimSpriteDefinition implements Comparable<AnimSpriteDefinition>, Serializable {

    public int index;
    public String spriteSheetName;
    public int direction;
    public int action;
    public SpriteDefinition spriteData;

    @Override
    public int compareTo(@NotNull AnimSpriteDefinition o) {
        int actionVal = Integer.compare(action, o.action);
        return actionVal == 0 ? Integer.compare(direction, o.direction) : actionVal;
    }
}
