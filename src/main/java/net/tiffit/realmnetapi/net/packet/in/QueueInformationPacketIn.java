package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.api.event.EventHandler;
import net.tiffit.realmnetapi.api.event.QueueInformationEvent;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;

import java.io.DataInputStream;
import java.io.IOException;

public class QueueInformationPacketIn extends RotMGPacketIn {

    public int position;
    public int outOf;

    public void read(DataInputStream in) throws IOException {
        position = in.readUnsignedShort();
        outOf = in.readUnsignedShort();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        EventHandler.executeEvent(new QueueInformationEvent(position, outOf));
    }
}
