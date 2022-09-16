package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class PongPacketOut extends RotMGPacketOut {

    private int serial;
    private int time;

    public PongPacketOut(int serial) {
        super((byte)31);
        this.serial = serial;
        this.time = RealmNetworker.getTime();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(serial);
        out.writeInt(time);
    }

    @Override
    public String getExtraInfo() {
        return "{" + serial + ", time=" + time + "}";
    }
}
