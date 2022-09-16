package net.tiffit.realmnetapi.map;

import lombok.Getter;
import lombok.Setter;
import net.tiffit.realmnetapi.api.Hooks;
import net.tiffit.realmnetapi.api.IPlayerPosTracker;
import net.tiffit.realmnetapi.api.event.EventHandler;
import net.tiffit.realmnetapi.api.event.TileAddEvent;
import net.tiffit.realmnetapi.assets.xml.Ground;
import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.object.RObject;
import net.tiffit.realmnetapi.map.object.RotMGEntityList;
import net.tiffit.realmnetapi.map.projectile.RotMGProjectileList;
import net.tiffit.realmnetapi.util.RotMGRandom;
import net.tiffit.realmnetapi.util.math.Vec2f;
import net.tiffit.realmnetapi.util.math.Vec2i;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
public class RMap {

    private final int width, height;
    private final RotMGEntityList entityList = new RotMGEntityList();
    private final RotMGProjectileList projectileList = new RotMGProjectileList();
    private final Ground[][] tiles;
    private final RObject[][] staticObjects;
    private final String name, displayName, realmName;

    @Setter
    private RotMGRandom random;
    @Setter
    private int objectId;
    @Setter
    private GameObjectState selfState;
    private final IPlayerPosTracker playerPos = Hooks.PlayerPosTracker.get();

    @Setter
    private int lastTickId = -1;

    public RMap(int width, int height, String name, String displayName, String realmName) {
        this.width = width;
        this.height = height;
        this.name = name;
        this.displayName = displayName;
        this.realmName = realmName;
        tiles = new Ground[width][height];
        staticObjects = new RObject[width][height];
    }

    public void setTiles(HashMap<Vec2i, Ground> newTiles){
        newTiles.forEach((vec2i, ground) -> tiles[vec2i.x()][vec2i.y()] = ground);
        EventHandler.executeEvent(new TileAddEvent(this, newTiles));
    }

    public RObject getStaticGameObject(int x, int y){
        if(!inMap(x, y) || staticObjects[x][y] == null){
            return null;
        }
        return staticObjects[x][y];
    }

    public void setStaticGameObject(int x, int y, RObject object){
        if(inMap(x, y)){
            staticObjects[x][y] = object;
        }
    }

    public boolean inMap(double x, double y){
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public RObject getClosestGameObject(float maxDistance, String... validClasses){
        RObject closest = null;
        float curDistance = maxDistance;
        Vec2f playerPos = getPlayerPos().getPos();
        List<RObject> objs = getEntityList().getAll(object -> Arrays.stream(validClasses).anyMatch(s -> s.equals(object.getGameObject().goClass)));
        for (RObject object : objs) {
            float objPos = playerPos.distanceSqr(object.getCurrentPos());
            if(objPos < curDistance){
                closest = object;
                curDistance = objPos;
            }
        }
        return closest;
    }
}
