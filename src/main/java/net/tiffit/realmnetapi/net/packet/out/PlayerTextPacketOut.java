package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class PlayerTextPacketOut extends RotMGPacketOut {

    public String text;

    public PlayerTextPacketOut(String text) {
        super((byte)10);
        this.text = text;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(text);
    }
}
