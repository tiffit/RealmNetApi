package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;

import java.io.DataInputStream;
import java.io.IOException;

public class ClientStatPacketIn extends RotMGPacketIn  {

    public String name;
    public int value;

    @Override
    public void read(DataInputStream in) throws IOException {
        name = in.readUTF();
        value = in.readInt();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
    }

    @Override
    public String getExtraInfo() {
        return "{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

}

