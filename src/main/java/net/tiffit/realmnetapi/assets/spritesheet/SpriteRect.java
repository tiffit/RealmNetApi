package net.tiffit.realmnetapi.assets.spritesheet;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class SpriteRect implements Serializable {

    public int x, y, w, h;

    public boolean isAllZero(){
        return x == 0 && y == 0 && w == 0 && h == 0;
    }

}
