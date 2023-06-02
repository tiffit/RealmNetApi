package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class LoadPacketOut extends RotMGPacketOut {

    private final int characterId;

    public LoadPacketOut(int characterId) {
        super((byte)61);
        this.characterId = characterId;
    }
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(characterId);
        out.writeBoolean(false);
    }
}
