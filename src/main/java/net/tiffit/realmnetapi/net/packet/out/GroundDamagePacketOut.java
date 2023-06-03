package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataOutput;
import java.io.IOException;

public class GroundDamagePacketOut extends RotMGPacketOut {

    private final int time;
    private final Vec2f pos;

    public GroundDamagePacketOut(int time, Vec2f pos) {
        super((byte)103);
        this.time = time;
        this.pos = pos;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(time);
        writeWorldPosData(out, pos);
    }

    @Override
    public String getExtraInfo() {
        return "{time=" + time + ", pos=" + pos + "}";
    }
}
