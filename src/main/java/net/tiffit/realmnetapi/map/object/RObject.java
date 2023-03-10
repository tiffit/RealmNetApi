package net.tiffit.realmnetapi.map.object;

import lombok.Getter;
import net.tiffit.realmnetapi.api.IObjectListener;
import net.tiffit.realmnetapi.assets.xml.GameObject;
import net.tiffit.realmnetapi.assets.xml.XMLLoader;
import net.tiffit.realmnetapi.map.RMap;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.util.math.Vec2f;

@Getter
public class RObject {

    private final RMap map;
    private final GameObjectState state;
    private final GameObject gameObject;
    private final IObjectListener<RObject> listener;

    private Vec2f posAtTick = Vec2f.ZERO;
    private Vec2f tickPosition = Vec2f.ZERO;
    private Vec2f moveVec = Vec2f.ZERO;
    private Vec2f currentPos = Vec2f.ZERO;
    private int lastTickUpdateTime = -1;
    private int localTickId = -1;
    private boolean dead = false;

    public RObject(GameObjectState state, RMap map){
        this.state = state;
        this.map = map;
        this.gameObject = XMLLoader.OBJECTS.get(state.type);
        spawnAt(state.position.x(), state.position.y());
        listener = map.getNet().hooks.ObjectListener.apply(this);
    }

    public void mergeState(GameObjectState state){
        this.state.merge(state);
    }

    public void kill(){
        this.dead = true;
        if(getState().getGameObject().staticObject){
            map.setStaticGameObject((int)getCorrectedX(), (int)getCorrectedY(), null);
        }
        listener.objectKill();
    }

    public boolean isDead(){
        return dead;
    }

    public void updateLoop(){
        if (!(moveVec.x() == 0 && moveVec.y() == 0)) {
            if (localTickId < map.getLastTickId()){
                moveVec = Vec2f.ZERO;
                moveTo(tickPosition.x(), tickPosition.y());
            }else{
                int dt = RealmNetworker.getTime() - lastTickUpdateTime;
                float newX = posAtTick.x() + dt * moveVec.x();
                float newZ = posAtTick.y() + dt * moveVec.y();
                moveTo(newX, newZ);
            }
        }
        listener.updateLoop();
    }

    public void onGoto(float x, float y, int tickTime){
        moveTo(x, y);
        lastTickUpdateTime = tickTime;
        tickPosition = new Vec2f(x, y);
        posAtTick = new Vec2f(x, y);
        moveVec = Vec2f.ZERO;
    }

    public void onTickPos(float x, float y, int newTickTime, int tickId){
        if(localTickId < map.getLastTickId()){
            moveTo(tickPosition.x(), tickPosition.y());
        }
        lastTickUpdateTime = RealmNetworker.getTime();//net.lastUpdate;
        tickPosition = new Vec2f(x, y);
        posAtTick = new Vec2f(getCorrectedX(), getCorrectedY());
        moveVec = new Vec2f((tickPosition.x() - posAtTick.x())/newTickTime,(tickPosition.y() - posAtTick.y())/newTickTime);
        localTickId = tickId;
    }

    public void spawnAt(float x, float z){
        moveTo(x, z);
        tickPosition = new Vec2f(x, z);
    }

    public void moveTo(float x, float y){
        Vec2f oldPos = new Vec2f(currentPos.x(), currentPos.y());
        currentPos = new Vec2f(x, y);
        if(getState().getGameObject().staticObject){
            map.setStaticGameObject((int)oldPos.x(), (int)oldPos.y(), null);
            map.setStaticGameObject((int)x, (int)y, this);
        }
    }

    public float getCorrectedX(){
        return currentPos.x();
    }

    public float getCorrectedY(){
        return currentPos.y();
    }

}
