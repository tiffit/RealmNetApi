package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class GotoAckPacketOut extends RotMGPacketOut {

    private final int time;

    public GotoAckPacketOut(int time) {
        super((byte)65);
        this.time = time;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(time);
        out.writeBoolean(false);
    }
}
