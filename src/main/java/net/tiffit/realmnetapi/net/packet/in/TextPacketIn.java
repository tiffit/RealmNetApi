package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.api.event.ChatEvent;
import net.tiffit.realmnetapi.api.event.EventHandler;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;

import java.io.DataInputStream;
import java.io.IOException;

public class TextPacketIn extends RotMGPacketIn {

    public String name;
    public int objectId;
    public int numStars;
    public int bubbleTime;
    public String recipient;
    public String text;
    public String cleanText;
    public boolean isSupporter;
    public int starBackground;

    @Override
    public void read(DataInputStream in) throws IOException {
        name = in.readUTF();
        objectId = in.readInt();
        numStars = in.readShort();
        bubbleTime = in.readUnsignedByte();
        recipient = in.readUTF();
        text = in.readUTF();
        cleanText = in.readUTF();
        isSupporter = in.readBoolean();
        starBackground = in.readInt();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        EventHandler.executeEvent(new ChatEvent(this));
    }

    @Override
    public String getExtraInfo() {
        return "{" + text + "}";
    }
}
