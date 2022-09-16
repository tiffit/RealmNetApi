package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.api.event.EventHandler;
import net.tiffit.realmnetapi.api.event.ReconnectEvent;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;

import java.io.DataInputStream;
import java.io.IOException;

public class ReconnectPacketIn extends RotMGPacketIn {

    public String name;
    public String host;
    public int unknown;
    public int gameId;
    public int keyTime;
    public byte[] key;

    @Override
    public void read(DataInputStream in) throws IOException {
        name = in.readUTF();
        host = in.readUTF();
        unknown = in.readUnsignedShort();
        gameId = in.readInt();
        keyTime = in.readInt();
        key = new byte[in.readShort()];
        in.readFully(key);
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        System.out.println("Loading " + name + "= " + host + ":" + gameId);
        EventHandler.executeEvent(new ReconnectEvent(this));
    }
}
