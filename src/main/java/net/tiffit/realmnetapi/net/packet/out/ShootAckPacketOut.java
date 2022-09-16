package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class ShootAckPacketOut extends RotMGPacketOut {

    private int time;
    private short ackCount;

    public ShootAckPacketOut(int time, short ackCount) {
        super((byte)121);
        this.time = time;
        this.ackCount = ackCount;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(time);
        out.writeShort(ackCount);
    }

    @Override
    public String getExtraInfo() {
        return "{" +
                "time=" + time +
                ", ackCount=" + ackCount +
                '}';
    }
}
