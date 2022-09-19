package net.tiffit.realmnetapi.util.math;

public record Vec2f(float x, float y) {

    public static Vec2f ZERO = new Vec2f(0, 0);

    public boolean isZero(){
        return x == 0 && y == 0;
    }

    public float distanceSqr(Vec2f other){
        return (other.x - x)*(other.x - x) + (other.y - y)*(other.y - y);
    }

    public Vec2f add(Vec2f other){
        return new Vec2f(this.x + other.x, this.y + other.y);
    }

    public Vec2f add(float x, float y){
        return add(new Vec2f(x, y));
    }

    public Vec2f sub(Vec2f other){
        return new Vec2f(this.x - other.x, this.y - other.y);
    }

    public Vec2f sub(float x, float y){
        return sub(new Vec2f(x, y));
    }

    public Vec2f withX(float value){
        return new Vec2f(value, y);
    }

    public Vec2f withY(float value){
        return new Vec2f(x, value);
    }

    public Vec2f absolute(){
        return new Vec2f(Math.abs(x), Math.abs(y));
    }

    public static Vec2f rotate(float distance, float angleRad){
        return new Vec2f((float)(distance * Math.cos(angleRad)), (float)(distance * Math.sin(angleRad)));
    }
}
