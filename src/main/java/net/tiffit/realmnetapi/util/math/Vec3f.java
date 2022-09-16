package net.tiffit.realmnetapi.util.math;

public record Vec3f(float x, float y, float z) {

    public static Vec3f ZERO = new Vec3f(0, 0, 0);

    public boolean isZero(){
        return x == 0 && y == 0 && z == 0;
    }
}
