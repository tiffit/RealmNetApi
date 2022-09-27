package net.tiffit.realmnetapi.util;

import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;

import java.io.DataInputStream;
import java.io.IOException;

public record DeathFameInfo(String achievement, int fameLevel, int fameAdded){

    public static DeathFameInfo deserialize(DataInputStream in) throws IOException {
        return new DeathFameInfo(in.readUTF(), RotMGPacketIn.readCompressedInt(in), RotMGPacketIn.readCompressedInt(in));
    }

}
