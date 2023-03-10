package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.map.RMap;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;
import net.tiffit.realmnetapi.net.packet.out.LoadPacketOut;
import net.tiffit.realmnetapi.util.RotMGRandom;

import java.io.DataInputStream;
import java.io.IOException;

public class MapInfoPacketIn extends RotMGPacketIn {

    public int width;
    public int height;
    public String name;
    public String displayName;
    public String realmName;
    public float difficulty;
    public long seed;
    public int background;
    public boolean allowPlayerTeleport;
    public boolean showDisplays;
    public boolean unknownBoolean;
    public short maxPlayers;
    public long gameOpenedTime;
    public String buildVersion;
    public int unknownInt;
    public String dungeonModifiers;

    @Override
    public void read(DataInputStream in) throws IOException {
        width = in.readInt();
        height = in.readInt();
        name = in.readUTF();
        displayName = in.readUTF();
        realmName = in.readUTF();
        seed = readUnsignedInteger(in);
        background = in.readInt();
        difficulty = in.readFloat();
        allowPlayerTeleport = in.readBoolean();
        showDisplays = in.readBoolean();
        unknownBoolean = in.readBoolean();
        maxPlayers = in.readShort();
        gameOpenedTime = readUnsignedInteger(in);
        buildVersion = in.readUTF();
        unknownInt = in.readInt();
        dungeonModifiers = in.readUTF();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        net.send(new LoadPacketOut(net.hooks.CharacterId));
        net.map = new RMap(net, width, height, name, displayName, realmName, allowPlayerTeleport);
        net.map.setRandom(new RotMGRandom(seed));
    }
}
