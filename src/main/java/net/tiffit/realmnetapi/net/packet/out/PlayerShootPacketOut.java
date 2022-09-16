package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataOutput;
import java.io.IOException;

public class PlayerShootPacketOut extends RotMGPacketOut {

    public int time;
    public short bulletId;
    public short weaponId;
    public short projectileId;
    public Vec2f pos;
    public float angle;
    public boolean isBurst;

    public PlayerShootPacketOut(int time, short bulletId, short weaponId, byte projectileId, Vec2f pos, float angle, boolean isBurst) {
        super((byte)30);
        this.time = time;
        this.bulletId = bulletId;
        this.weaponId = weaponId;
        this.projectileId = projectileId;
        this.pos = pos;
        this.angle = angle;
        this.isBurst = isBurst;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(time);
        out.writeShort(bulletId);
        out.writeShort(weaponId);
        out.writeByte(projectileId);
        writeWorldPosData(out, pos);
        out.writeFloat(angle);
        out.writeBoolean(isBurst);
    }

    @Override
    public String getExtraInfo() {
        return "{time=" + time + ", bulletId=" + bulletId + "}";
    }
}
