package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.api.event.CreateSuccessEvent;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;

import java.io.DataInputStream;
import java.io.IOException;

public class CreateSuccessPacketIn extends RotMGPacketIn {

    public int objectId;
    public int charId;

    @Override
    public void read(DataInputStream in) throws IOException {
        objectId = in.readInt();
        charId = in.readInt();
        in.readUTF();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        net.map.setObjectId(objectId);
        net.map.setCharacterId(charId);
        net.eventHandler.executeEvent(new CreateSuccessEvent(objectId, charId));
    }
}
