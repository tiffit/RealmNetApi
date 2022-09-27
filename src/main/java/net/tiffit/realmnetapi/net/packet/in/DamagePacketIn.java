package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.api.event.DamageEvent;
import net.tiffit.realmnetapi.api.event.EventHandler;
import net.tiffit.realmnetapi.assets.ConditionEffect;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DamagePacketIn extends RotMGPacketIn  {

    public int targetId;
    public List<ConditionEffect> effects = new ArrayList<>();
    public int damageAmount;
    public boolean kill;
    public boolean armorPierce;
    public int bulletId;
    public int objectId;

    @Override
    public void read(DataInputStream in) throws IOException {
        this.targetId = in.readInt();
        int effectCount = in.readUnsignedByte();
        for(int i = 0; i < effectCount; i++){
            effects.add(ConditionEffect.byId(in.readUnsignedByte()));
        }
        damageAmount = in.readUnsignedShort();
        kill = in.readBoolean();
        armorPierce = in.readBoolean();
        bulletId = in.readUnsignedByte();
        objectId = in.readInt();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        EventHandler.executeEvent(new DamageEvent(this));
    }
}

