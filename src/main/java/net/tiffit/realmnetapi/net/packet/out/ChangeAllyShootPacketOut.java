package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class ChangeAllyShootPacketOut extends RotMGPacketOut {

    public int enable;

    public ChangeAllyShootPacketOut(boolean enable) {
        super((byte)122);
        this.enable = enable ? 1 : 0;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(enable);
    }
}
