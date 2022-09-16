package net.tiffit.realmnetapi.assets.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Animation implements Serializable {

    public String id;
    public int index;
    public float period;
    public float prob;
    public boolean sync;
    public List<AnimationFrame> frames = new ArrayList<>();

    public static class AnimationFrame implements Serializable{
        public float time;
        public Texture texture;
    }
}
