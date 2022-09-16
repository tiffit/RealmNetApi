package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.api.event.EventHandler;
import net.tiffit.realmnetapi.api.event.PlayerDataEvent;
import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.object.MoveRecordState;
import net.tiffit.realmnetapi.map.object.RObject;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;
import net.tiffit.realmnetapi.net.packet.out.MovePacketOut;
import net.tiffit.realmnetapi.util.ShootAckCounter;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class NewTickPacketIn extends RotMGPacketIn {

    public int tickId;
    public int tickTime;
    public int serverRealTimeMS;
    public int serverLastRTMS;
    private GameObjectState[] objects;

    @Override
    public void read(DataInputStream in) throws IOException {
        this.tickId = in.readInt();
        this.tickTime = in.readInt();
        this.serverRealTimeMS = in.readInt();
        this.serverLastRTMS = in.readUnsignedShort();

        objects = new GameObjectState[in.readShort()];
        for(short i = 0; i < objects.length; i++) {
            objects[i] = GameObjectState.read(false, in);
        }
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        ShootAckCounter.ack(net, RealmNetworker.getTime());
//        for (Integer shootAck : ShootAckCounter.getValues()) {
//            if(shootAck == -1){
//                net.send(new ShootAckPacketOut(-1, (short)1));
//            }else{
//                net.send(new ShootAckPacketOut(time, shootAck.shortValue()));
//                ShootAckCounter.ack(shootAck);
//            }
//        }
        try {
            net.LOCK.lock();
            net.updateCondition.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            net.LOCK.unlock();
        }
        int time = RealmNetworker.getTime();
        List<MoveRecordState> records = new LinkedList<>();
        net.records.lock.lock();
        if(net.records.lastClear >= 0){
            int length = Math.min(10, net.records.records.size());
            for(int i = 0; i < length; i++){
                records.add(net.records.records.get(i));
            }
        }
        net.records.clear(time);
        net.records.lock.unlock();
        Vec2f pos = net.map.getPlayerPos().getPos();
        if(records.size() == 0 || records.get(records.size() -1).time < time){
            records.add(new MoveRecordState(time, pos.x(), pos.y()));
        }
        net.send(new MovePacketOut(tickId, serverRealTimeMS, records));

        for (GameObjectState state : objects) {
            if(state.objectId == net.map.getObjectId()){
                net.map.getSelfState().merge(state);
                EventHandler.executeEvent(new PlayerDataEvent(net.map.getSelfState()));
            }else {
                RObject entity = net.map.getEntityList().get(state.objectId);
                if (tickTime != 0 && entity != null) {
                    entity.mergeState(state);
                    entity.onTickPos(state.position.x(), state.position.y(), tickTime, tickId);
                }
            }
        }
        net.map.setLastTickId(tickId);
    }

    @Override
    public String getExtraInfo() {
        return "{" + tickId + "}";
    }
}
