package net.tiffit.realmnetapi.net.packet;

import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataOutput;
import java.io.IOException;

public abstract class RotMGPacketOut {

    public final byte id;

    public RotMGPacketOut(byte id){
        this.id = id;
    }

    public abstract void write(DataOutput out) throws IOException;

    protected void writeWorldPosData(DataOutput out, Vec2f pos) throws IOException {
        out.writeFloat(pos.x());
        out.writeFloat(pos.y());
    }

    public String toString(){
        return getClass().getSimpleName();
    }

    public String getExtraInfo(){return "";}
}
