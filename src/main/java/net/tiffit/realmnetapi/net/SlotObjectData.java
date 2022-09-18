package net.tiffit.realmnetapi.net;

import java.io.DataOutput;
import java.io.IOException;

public record SlotObjectData(int objId, int slot, int type){

    public void write(DataOutput out) throws IOException {
        out.writeInt(objId);
        out.writeInt(slot);
        out.writeInt(type);
    }

}