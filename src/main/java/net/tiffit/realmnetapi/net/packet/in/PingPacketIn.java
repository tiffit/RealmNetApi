package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;
import net.tiffit.realmnetapi.net.packet.out.PongPacketOut;

import java.io.DataInputStream;
import java.io.IOException;

public class PingPacketIn extends RotMGPacketIn {

    public int serial;

    @Override
    public void read(DataInputStream in) throws IOException {
        serial = in.readInt();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        net.ackHandler.add(() -> net.send(new PongPacketOut(serial)));
    }

    @Override
    public String getExtraInfo() {
        return "{" + serial + "}";
    }
}
