package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class TeleportPacketOut extends RotMGPacketOut {

    private final int objectId;
    private final String name;

    public TeleportPacketOut(int objectId, String name) {
        super((byte)74);
        this.objectId = objectId;
        this.name = name;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(objectId);
        out.writeUTF(name);
    }
}
