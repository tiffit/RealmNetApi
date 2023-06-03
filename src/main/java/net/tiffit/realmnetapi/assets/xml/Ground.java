package net.tiffit.realmnetapi.assets.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ground implements Serializable {
    public int type;
    public String id;
    public boolean nowalk = false;
    public boolean sink = false;
    public float speed = 1;

    public boolean push;

    public boolean alpha;
    public boolean sameTypeEdgeMode;
    public TileAnimate animate;
    public int color;
    public List<Texture> textures = new ArrayList<>();
    public Texture edge, innerCorner, corner;

    public int minDamage = 0, maxDamage = 0;
}
