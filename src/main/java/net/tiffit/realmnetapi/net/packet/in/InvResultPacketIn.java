package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.SlotObjectData;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;

import java.io.DataInputStream;
import java.io.IOException;

public class InvResultPacketIn extends RotMGPacketIn {

    public boolean unknown;
    public boolean isUsed;
    public SlotObjectData fromSlot;
    public SlotObjectData toSlot;
    public int unknown2;
    public int unknown3;

    @Override
    public void read(DataInputStream in) throws IOException {
        unknown = in.readBoolean();
        isUsed = in.readBoolean();
        fromSlot = SlotObjectData.read(in);
        toSlot = SlotObjectData.read(in);
        unknown2 = in.readInt();
        unknown3 = in.readInt();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {}
}
