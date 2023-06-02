package net.tiffit.realmnetapi.util.math;

public record Vec3f(float x, float y, float z) {

    public static Vec3f ZERO = new Vec3f(0, 0, 0);

    public boolean isZero(){
        return x == 0 && y == 0 && z == 0;
    }

    public Vec3f add(float x, float y, float z){
        return new Vec3f(this.x + x, this.y + y,this.z + z);
    }

    public float distanceSq(Vec3f o){
        float dx = x-o.x;
        float dy = y-o.y;
        float dz = z-o.z;
        return dx*dx + dy*dy + dz*dz;
    }

    public float distanceSqXZ(Vec3f o){
        float dx = x-o.x;
        float dz = z-o.z;
        return dx*dx + dz*dz;
    }
}
