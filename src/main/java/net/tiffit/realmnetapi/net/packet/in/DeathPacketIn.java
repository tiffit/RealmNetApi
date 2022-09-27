package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.api.event.DeathEvent;
import net.tiffit.realmnetapi.api.event.EventHandler;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;
import net.tiffit.realmnetapi.util.DeathFameInfo;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class DeathPacketIn extends RotMGPacketIn  {

    public String accountId;
    public int characterId;
    public String killedBy;
    public int gravestoneType;
    public int totalFame;
    public DeathFameInfo[] fameInfo;
    public String unknownString;

    @Override
    public void read(DataInputStream in) throws IOException {
        accountId = in.readUTF();
        characterId = readCompressedInt(in);
        killedBy = in.readUTF();
        gravestoneType = in.readInt();
        totalFame = readCompressedInt(in);
        fameInfo = new DeathFameInfo[readCompressedInt(in)];
        for (int i = 0; i < fameInfo.length; i++) {
            fameInfo[i] = DeathFameInfo.deserialize(in);
        }
        unknownString = in.readUTF();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        EventHandler.executeEvent(new DeathEvent(this));
        net.ackHandler.add(net::disconnect);
    }

    @Override
    public String getExtraInfo() {
        return "{" +
                "accountId='" + accountId + '\'' +
                ", characterId=" + characterId +
                ", killedBy='" + killedBy + '\'' +
                ", gravestoneType=" + gravestoneType +
                ", totalFame=" + totalFame +
                ", fameInfo=" + Arrays.toString(fameInfo) +
                ", unknownString='" + unknownString + '\'' +
                '}';
    }
}

