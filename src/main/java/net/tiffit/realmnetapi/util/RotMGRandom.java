package net.tiffit.realmnetapi.util;

public class RotMGRandom {

    private long seed;

    public RotMGRandom(long seed){
        this.seed = seed;
    }

    public int nextIntRange(int lower, int upper){
        return lower == upper ? lower : lower + gen() % (upper - lower);
    }

    private int gen(){
        long offset = 16807 * (seed >> 16);
        long newSeed = 16807 * (seed & 65535);
        newSeed += (offset & 32767) << 16;
        newSeed += offset >> 15;
        if(newSeed > Integer.MAX_VALUE){
            newSeed -= Integer.MAX_VALUE;
        }
        this.seed = newSeed;
        return (int)seed;
    }

}