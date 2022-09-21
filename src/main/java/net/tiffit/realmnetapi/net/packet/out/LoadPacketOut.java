package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.RealmNetApi;
import net.tiffit.realmnetapi.auth.RotmgEnv;
import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;

public class LoadPacketOut extends RotMGPacketOut {

    public LoadPacketOut() {
        super((byte)57);
    }
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(RealmNetApi.ENV == RotmgEnv.PRODUCTION ? 1 : 84);
        out.writeBoolean(false);
    }
}
