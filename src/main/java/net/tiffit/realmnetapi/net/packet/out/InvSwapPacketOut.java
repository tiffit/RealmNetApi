package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.SlotObjectData;
import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataOutput;
import java.io.IOException;

public class InvSwapPacketOut extends RotMGPacketOut {

    private int time;
    private Vec2f pos;

    private SlotObjectData data1;
    private SlotObjectData data2;

    public InvSwapPacketOut(int time, Vec2f pos, SlotObjectData data1, SlotObjectData data2) {
        super((byte)95);
        this.time = time;
        this.pos = pos;
        this.data1 = data1;
        this.data2 = data2;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(time);
        writeWorldPosData(out, pos);
        data1.write(out);
        data2.write(out);
    }
}
