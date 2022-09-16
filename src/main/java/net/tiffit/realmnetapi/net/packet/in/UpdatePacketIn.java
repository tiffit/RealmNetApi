package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.api.Hooks;
import net.tiffit.realmnetapi.api.event.EventHandler;
import net.tiffit.realmnetapi.api.event.PlayerDataEvent;
import net.tiffit.realmnetapi.assets.xml.GameObject;
import net.tiffit.realmnetapi.assets.xml.Ground;
import net.tiffit.realmnetapi.assets.xml.XMLLoader;
import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.object.RObject;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;
import net.tiffit.realmnetapi.net.packet.out.UpdateAckPacketOut;
import net.tiffit.realmnetapi.util.ShootAckCounter;
import net.tiffit.realmnetapi.util.math.Vec2f;
import net.tiffit.realmnetapi.util.math.Vec2i;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class UpdatePacketIn extends RotMGPacketIn {

    public Vec2f playerPos;
    public HashMap<Vec2i, Ground> tiles = new HashMap<>();
    public GameObjectState[] objects;
    public int[] drops;

    @Override
    public void read(DataInputStream in) throws IOException {
        playerPos = readWorldPosData(in);
        in.readByte();
        // Tiles
        int numTiles = readCompressedInt(in);
        for (int i = 0; i < numTiles; i++) {
            short x = in.readShort();
            short y = in.readShort();
            int type = in.readUnsignedShort();
            tiles.put(new Vec2i(x, y), XMLLoader.GROUNDS.get(type));
        }
        // Objects
        objects = new GameObjectState[readCompressedInt(in)];
        for(int i = 0; i < objects.length; i++) {
            objects[i] = GameObjectState.read(true, in);
        }
        // Drops
        drops = new int[readCompressedInt(in)];
        for (int i = 0; i < drops.length; i++) {
            drops[i] = readCompressedInt(in);
        }
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        ShootAckCounter.ack(net, RealmNetworker.getTime());
//        for (Integer shootAck : ShootAckCounter.getValues()) {
//            if(shootAck == -1){
//                net.send(new ShootAckPacketOut(-1, (short)1));
//            }else{
//                net.send(new ShootAckPacketOut(RealmNetworker.getTime(), shootAck.shortValue()));
//            }
//        }
//        if(net.map.getShootCount().get() > 0){
//            int shootCount = net.map.getShootCount().getAndSet(0);
//            net.send(new ShootAckPacketOut(RealmNetworker.getTime(), (short)shootCount));
//        }
        Vec2f currentPlayerPos = net.map.getPlayerPos().getPos();
        if(currentPlayerPos.isZero()){
            net.records.clear(RealmNetworker.getTime());
            net.records.addRecord(RealmNetworker.getTime(), playerPos.x(), playerPos.y());
        }
        if(!playerPos.isZero() && currentPlayerPos.distanceSqr(playerPos) > 25){
            net.map.getPlayerPos().setPos(playerPos);
        }
        net.map.setTiles(tiles);

        for (GameObjectState state : objects) {
            GameObject go = XMLLoader.OBJECTS.get(state.type);
            if(net.map.getObjectId() == state.objectId){
                net.map.setSelfState(state);
                EventHandler.executeEvent(new PlayerDataEvent(state));
            }else {
                if (go != null) {
                    RObject obj = Hooks.RObjectFunc.apply(state, net.map);
                    net.map.getEntityList().set(state.objectId, obj);
                }else{
                    System.out.println("Unknown object type " + state.type);
                }
            }
        }

        for (int id : drops) {
            RObject obj = net.map.getEntityList().get(id);
            if(obj != null){
                obj.kill();
                net.map.getEntityList().remove(id);
            }
        }
        try {
            net.LOCK.lock();
            net.updateCondition.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            net.LOCK.unlock();
        }
        net.send(new UpdateAckPacketOut());
    }

    @Override
    public String getExtraInfo() {
        StringBuilder builder = new StringBuilder("{objects=[");
        builder.append(Arrays.stream(objects).map(obj -> "" + obj.objectId).collect(Collectors.joining(",")));
        builder.append("],drops=[");
        builder.append(Arrays.stream(drops).boxed().map(integer -> "" + integer).collect(Collectors.joining(",")));
        builder.append("]}");
        return builder.toString();
    }
}
