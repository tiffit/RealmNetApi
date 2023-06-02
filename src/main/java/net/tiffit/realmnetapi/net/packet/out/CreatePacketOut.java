package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class CreatePacketOut extends RotMGPacketOut {

    private final short classType;
    private final short skinType;
    private final boolean isChallenger;
    private final boolean isSeasonal;

    public CreatePacketOut(short classType, short skinType, boolean isChallenger, boolean isSeasonal) {
        super((byte)57);
        this.classType = classType;
        this.skinType = skinType;
        this.isChallenger = isChallenger;
        this.isSeasonal = isSeasonal;
    }
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeShort(classType);
        out.writeShort(skinType);
        out.writeBoolean(isChallenger);
        out.writeBoolean(isSeasonal);
    }
}
