package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.api.event.AoeEvent;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;
import net.tiffit.realmnetapi.net.packet.out.AoeAckPacketOut;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataInputStream;
import java.io.IOException;

public class AoePacketIn extends RotMGPacketIn {

    public Vec2f pos;
    public float radius;
    public int damage;
    public int effect;
    public float duration;
    public int origType;
    public int color;
    public boolean armorPierce;

    public void read(DataInputStream in) throws IOException {
        pos = new Vec2f(in.readFloat(), in.readFloat());
        radius = in.readFloat();
        damage = in.readUnsignedShort();
        effect = in.readUnsignedByte();
        duration = in.readFloat();
        origType = in.readUnsignedShort();
        color = in.readInt();
        armorPierce = in.readBoolean();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        net.ackHandler.add(() -> {
            net.send(new AoeAckPacketOut(RealmNetworker.getTime(), net.map.getPlayerPos().getPos()));
            net.eventHandler.executeEvent(new AoeEvent(this));
        });
    }
}
