package net.tiffit.realmnetapi.net.packet;

import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.in.*;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Supplier;

public abstract class RotMGPacketIn {

    public static HashMap<Integer, Supplier<RotMGPacketIn>> PACKET_MAP = new HashMap<>();

    public static void init(){
        PACKET_MAP.clear();
        PACKET_MAP.put(0, FailurePacketIn::new);
        PACKET_MAP.put(8, PingPacketIn::new);
        PACKET_MAP.put(9, NewTickPacketIn::new);
        PACKET_MAP.put(12, ServerPlayerShootPacketIn::new);
        PACKET_MAP.put(13, ShowEffectPacketIn::new);
        PACKET_MAP.put(18, GotoPacketIn::new);
        PACKET_MAP.put(35, EnemyShootPacketIn::new);
        PACKET_MAP.put(44, TextPacketIn::new);
        PACKET_MAP.put(45, ReconnectPacketIn::new);
        PACKET_MAP.put(46, DeathPacketIn::new);
        PACKET_MAP.put(49, AllyShootPacketIn::new);
        PACKET_MAP.put(62, UpdatePacketIn::new);
        PACKET_MAP.put(64, AoePacketIn::new);
        PACKET_MAP.put(75, DamagePacketIn::new);
        PACKET_MAP.put(92, MapInfoPacketIn::new);
        PACKET_MAP.put(101, CreateSuccessPacketIn::new);
    }

    public abstract void read(DataInputStream in) throws IOException;
    public abstract void handle(RealmNetworker net) throws IOException;

    public static long readUnsignedInteger(DataInput in) throws IOException {
        return Integer.toUnsignedLong(in.readInt());
    }

    public static Vec2f readWorldPosData(DataInput in) throws IOException {
        return new Vec2f(in.readFloat(), in.readFloat());
    }

    public static int readCompressedInt(DataInput in) throws IOException {
        int var3 = in.readUnsignedByte();
        boolean var4 = (var3 & 64) != 0;
        int var5 = 6;
        int var2 = var3 & 63;
        while((var3 & 128) != 0){
            var3 = in.readUnsignedByte();
            var2 = var2 | (var3 & 127) << var5;
            var5 += 7;
        }
        if(var4){
            var2 = -var2;
        }
        return var2;
    }

    public String getExtraInfo(){return "";}
}
