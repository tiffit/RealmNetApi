package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.SlotObjectData;
import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataOutput;
import java.io.IOException;

public class UseItemPacketOut extends RotMGPacketOut {

    private int time;
    private SlotObjectData data;
    private Vec2f pos;
    private int type;

    public UseItemPacketOut(int time, SlotObjectData data, Vec2f pos, int type) {
        super((byte)11);
        this.time = time;
        this.data = data;
        this.pos = pos;
        this.type = type;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(time);
        data.write(out);
        writeWorldPosData(out, pos);
        out.writeByte(type);
    }
}