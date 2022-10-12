package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;

import java.io.DataInputStream;
import java.io.IOException;

public class RealmHeroesLeftPacketIn extends RotMGPacketIn {

    public int heroesLeft;

    @Override
    public void read(DataInputStream in) throws IOException {
        heroesLeft = in.readInt();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        net.map.setHeroesRemaining(heroesLeft);
    }

    @Override
    public String getExtraInfo() {
        return "{" + heroesLeft + "}";
    }
}
