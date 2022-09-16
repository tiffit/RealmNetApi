package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class PlayerHitPacketOut extends RotMGPacketOut {

    private short bulletId;
    private int ownerId;

    public PlayerHitPacketOut(short bulletId, int ownerId) {
        super((byte)90);
        this.bulletId = bulletId;
        this.ownerId = ownerId;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeShort(this.bulletId);
        out.writeInt(this.ownerId);
    }

    @Override
    public String getExtraInfo() {
        return "{" +
                "bulletId=" + bulletId +
                ", ownerId=" + ownerId +
                '}';
    }
}