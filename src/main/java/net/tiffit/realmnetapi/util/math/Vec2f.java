package net.tiffit.realmnetapi.util.math;

import javax.annotation.CheckReturnValue;
import java.io.Serializable;

public record Vec2f(float x, float y) implements Serializable {

    public static Vec2f ZERO = new Vec2f(0, 0);

    @CheckReturnValue
    public boolean isZero(){
        return x == 0 && y == 0;
    }

    @CheckReturnValue
    public float distanceSqr(Vec2f other){
        return (other.x - x)*(other.x - x) + (other.y - y)*(other.y - y);
    }

    @CheckReturnValue
    public Vec2f add(Vec2f other){
        return new Vec2f(this.x + other.x, this.y + other.y);
    }

    @CheckReturnValue
    public Vec2f add(float x, float y){
        return add(new Vec2f(x, y));
    }

    @CheckReturnValue
    public Vec2f sub(Vec2f other){
        return new Vec2f(this.x - other.x, this.y - other.y);
    }

    @CheckReturnValue
    public Vec2f sub(float x, float y){
        return sub(new Vec2f(x, y));
    }

    @CheckReturnValue
    public Vec2f withX(float value){
        return new Vec2f(value, y);
    }

    @CheckReturnValue
    public Vec2f withY(float value){
        return new Vec2f(x, value);
    }

    @CheckReturnValue
    public Vec2f absolute(){
        return new Vec2f(Math.abs(x), Math.abs(y));
    }

    @CheckReturnValue
    public static Vec2f rotate(float distance, float angleRad){
        return new Vec2f((float)(distance * Math.cos(angleRad)), (float)(distance * Math.sin(angleRad)));
    }

    @CheckReturnValue
    public Vec2f rotate(float angleRad){
        return new Vec2f((float)(x() * Math.cos(angleRad)), (float)(y() * Math.sin(angleRad)));
    }
}
