package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class UpdateAckPacketOut extends RotMGPacketOut {

    public UpdateAckPacketOut() {
        super((byte)81);
    }
    @Override
    public void write(DataOutput out) throws IOException {
    }
}
