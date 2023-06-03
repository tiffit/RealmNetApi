package net.tiffit.realmnetapi.map;

import lombok.Getter;
import lombok.Setter;
import net.tiffit.realmnetapi.api.IPlayerPosTracker;
import net.tiffit.realmnetapi.api.event.GroundDamageEvent;
import net.tiffit.realmnetapi.api.event.TileAddEvent;
import net.tiffit.realmnetapi.assets.ConditionEffect;
import net.tiffit.realmnetapi.assets.xml.Ground;
import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.object.RObject;
import net.tiffit.realmnetapi.map.object.RotMGEntityList;
import net.tiffit.realmnetapi.map.projectile.RotMGProjectileList;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.out.GroundDamagePacketOut;
import net.tiffit.realmnetapi.util.RotMGRandom;
import net.tiffit.realmnetapi.util.math.Vec2f;
import net.tiffit.realmnetapi.util.math.Vec2i;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
public class RMap {

    private final RealmNetworker net;
    private final int width, height;
    private final RotMGEntityList entityList = new RotMGEntityList();
    private final RotMGProjectileList projectileList = new RotMGProjectileList();
    private final Ground[][] tiles;
    private final RObject[][] staticObjects;
    private final int[][] lastDamage;
    private final String name, displayName, realmName;
    private final boolean allowTeleport;

    @Setter
    private RotMGRandom random;
    @Setter
    private int objectId;
    @Setter
    private int characterId;
    @Setter
    private GameObjectState selfState;
    private final IPlayerPosTracker playerPos;

    @Setter
    private int lastTickId = -1;

    @Setter
    private int heroesRemaining = -1;

    @Setter
    private int questObjectId = -1;

    public RMap(RealmNetworker net, int width, int height, String name, String displayName, String realmName, boolean allowTeleport) {
        this.net = net;
        this.width = width;
        this.height = height;
        this.name = name;
        this.displayName = displayName;
        this.realmName = realmName;
        this.allowTeleport = allowTeleport;
        tiles = new Ground[width][height];
        staticObjects = new RObject[width][height];
        lastDamage = new int[width][height];

        playerPos = net.hooks.PlayerPosTracker.get();
    }

    public void setTiles(HashMap<Vec2i, Ground> newTiles){
        if(newTiles.size() > 0){
            newTiles.forEach((vec2i, ground) -> tiles[vec2i.x()][vec2i.y()] = ground);
            net.eventHandler.executeEvent(new TileAddEvent(this, newTiles));
        }
    }

    public RObject getStaticGameObject(int x, int y){
        if(!inMap(x, y)){
            return null;
        }
        return staticObjects[x][y];
    }

    public Ground getTile(int x, int y){
        if(!inMap(x, y)){
            return null;
        }
        return tiles[x][y];
    }

    public void setStaticGameObject(int x, int y, RObject object){
        if(inMap(x, y)){
            staticObjects[x][y] = object;
        }
    }

    public boolean inMap(double x, double y){
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public void tickTileDamage(int time){
        if(selfState == null)return;
        Vec2f pos = getPlayerPos().getPos();
        if(!inMap(pos.x(), pos.y()))return;
        int tileX = (int)Math.floor(pos.x());
        int tileY = (int)Math.floor(pos.y());
        Ground tile = tiles[tileX][tileY];
        if(tile != null && tile.maxDamage > 0 && !selfState.hasEffect(ConditionEffect.INVINCIBLE) && time - lastDamage[tileX][tileY] >= 500){
            RObject staticObj = getStaticGameObject(tileX, tileY);
            if(staticObj != null && staticObj.getGameObject().protectFromGroundDamage)return;
            lastDamage[tileX][tileY] = time;
            net.send(new GroundDamagePacketOut(time, pos));
            net.eventHandler.executeEvent(new GroundDamageEvent(tile, pos));
        }
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
