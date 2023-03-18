package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.api.event.QuestUpdateEvent;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class QuestObjectIdPacketIn extends RotMGPacketIn {

    public int objectId;
    public int[] list;

    @Override
    public void read(DataInputStream in) throws IOException {
        objectId = in.readInt();
        list = new int[readCompressedInt(in)];
        for (int i = 0; i < list.length; i++) {
            list[i] = readCompressedInt(in);
        }
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        net.map.setQuestObjectId(objectId);
        net.eventHandler.executeEvent(new QuestUpdateEvent(objectId));
    }

    @Override
    public String toString() {
        return "{" +
                "objectId=" + objectId +
                ", list=" + Arrays.toString(list) +
                '}';
    }
}
