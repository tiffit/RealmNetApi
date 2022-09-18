package net.tiffit.realmnetapi.assets.spritesheet;

import java.io.Serializable;

public class SpriteColor implements Serializable {

    public short r, g, b, a;

    public int getABGR(){
        return (a << 8*3) | (b<<16) | (g<<8) | (r & 0x0ff);
    }

}
