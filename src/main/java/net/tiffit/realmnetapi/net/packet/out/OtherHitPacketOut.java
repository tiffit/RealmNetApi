package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class OtherHitPacketOut extends RotMGPacketOut {

    private int time;
    private short bulletId;
    private int ownerId;
    private int targetId;

    public OtherHitPacketOut(int time, short bulletId, int ownerId, int targetId) {
        super((byte)20);
        this.time = time;
        this.bulletId = bulletId;
        this.ownerId = ownerId;
        this.targetId = targetId;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(time);
        out.writeShort(this.bulletId);
        out.writeInt(this.ownerId);
        out.writeInt(this.targetId);
    }

    @Override
    public String getExtraInfo() {
        return "{time=" + time + ", bulletId=" + bulletId + ", ownerId=" + ownerId + ", targetId=" + targetId + "}";
    }
}
