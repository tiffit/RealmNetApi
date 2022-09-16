package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class EnemyHitPacketOut extends RotMGPacketOut {

    public int time;
    private short bulletId;
    private int shooterId;
    private int targetId;
    private boolean kill;
    private int mainId;

    public EnemyHitPacketOut(int time, short bulletId, int shooterId, int targetId, boolean kill, int mainId) {
        super((byte)25);
        this.time = time;
        this.bulletId = bulletId;
        this.shooterId = shooterId;
        this.targetId = targetId;
        this.kill = kill;
        this.mainId = mainId;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(time);
        out.writeShort(this.bulletId);
        out.writeInt(this.shooterId);
        out.writeInt(this.targetId);
        out.writeBoolean(kill);
        out.writeInt(this.mainId);
    }

    @Override
    public String getExtraInfo() {
        return "{" +
                "time=" + time +
                ", bulletId=" + bulletId +
                ", shooterId=" + shooterId +
                ", targetId=" + targetId +
                ", kill=" + kill +
                ", mainId=" + mainId +
                '}';
    }
}