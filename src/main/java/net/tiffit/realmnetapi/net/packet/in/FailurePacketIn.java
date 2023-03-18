package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.api.event.FailureEvent;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;

import java.io.DataInputStream;
import java.io.IOException;

public class FailurePacketIn extends RotMGPacketIn {

    private int code;
    private String description;

    @Override
    public void read(DataInputStream in) throws IOException {
        code = in.readInt();
        description = in.readUTF();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        net.disconnect();
        System.out.println("FAILURE: id=" + code + "; desc=" + description);
        net.eventHandler.executeEvent(new FailureEvent(code, description));
    }
}
