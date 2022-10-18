package net.tiffit.realmnetapi.util.math;

public record Rectf(float x, float y, float width, float height) {

    public Vec2f getStart(){
        return new Vec2f(x, y);
    }

    public Vec2f getEnd(){
        return new Vec2f(x + width, y + height);
    }

}
