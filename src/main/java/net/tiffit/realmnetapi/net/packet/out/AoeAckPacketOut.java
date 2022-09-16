package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataOutput;
import java.io.IOException;

public class AoeAckPacketOut extends RotMGPacketOut {

    private int time;
    private Vec2f position;

    public AoeAckPacketOut(int time, Vec2f position) {
        super((byte)89);
        this.time = time;
        this.position = position;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(time);
        writeWorldPosData(out, position);
    }
}