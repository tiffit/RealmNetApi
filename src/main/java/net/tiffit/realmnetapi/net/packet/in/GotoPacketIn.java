package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.object.RObject;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;
import net.tiffit.realmnetapi.net.packet.out.GotoAckPacketOut;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataInputStream;
import java.io.IOException;

public class GotoPacketIn extends RotMGPacketIn {

    public int objectId;
    public Vec2f position;
    public int unknown;

    @Override
    public void read(DataInputStream in) throws IOException {
        this.objectId = in.readInt();
        position = readWorldPosData(in);
        unknown = in.readInt();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        net.ackHandler.add(() -> {
            net.send(new GotoAckPacketOut(RealmNetworker.getTime()));
            if (objectId == net.map.getObjectId()) {
                net.map.getPlayerPos().setPos(position);
            } else {
                RObject obj = net.map.getEntityList().get(objectId);
                if (obj != null) {
                    GameObjectState state = obj.getState();
                    state.position = position;
                    obj.onGoto(position.x(), position.y(), RealmNetworker.getTime());
                }
            }
        });
    }

    @Override
    public String getExtraInfo() {
        return "{" + unknown + "}";
    }
}
