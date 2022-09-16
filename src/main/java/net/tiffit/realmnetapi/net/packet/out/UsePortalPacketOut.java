package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class UsePortalPacketOut extends RotMGPacketOut {

    private int objectId;

    public UsePortalPacketOut(int objectId) {
        super((byte)47);
        this.objectId = objectId;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(objectId);
    }
}
