package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.SlotObjectData;
import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class InvDropPacketOut extends RotMGPacketOut {

    private SlotObjectData data;
    private boolean unknown;

    public InvDropPacketOut(SlotObjectData data) {
        super((byte)19);
        this.data = data;
        this.unknown = false;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        data.write(out);
        out.writeBoolean(unknown);
    }
}
