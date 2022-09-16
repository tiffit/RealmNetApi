package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataInputStream;
import java.io.IOException;

public class ShowEffectPacketIn extends RotMGPacketIn {

    private static int EFFECT_BIT_COLOR = 1 << 0;

    private static int EFFECT_BIT_POS1_X = 1 << 1;

    private static int EFFECT_BIT_POS1_Y = 1 << 2;

    private static int EFFECT_BIT_POS2_X = 1 << 3;

    private static int EFFECT_BIT_POS2_Y = 1 << 4;

    private static int EFFECT_BIT_POS1 = EFFECT_BIT_POS1_X | EFFECT_BIT_POS1_Y;

    private static int EFFECT_BIT_POS2 = EFFECT_BIT_POS2_X | EFFECT_BIT_POS2_Y;

    private static int EFFECT_BIT_DURATION = 1 << 5;

    private static int EFFECT_BIT_ID = 1 << 6;

    public int effectType;
    public int targetObjectId;
    public Vec2f start;
    public Vec2f end;
    public int color;
    public double duration;
    public byte unknownByte;

    public void read(DataInputStream in) throws IOException {
        start = Vec2f.ZERO;
        end = Vec2f.ZERO;

        effectType = in.readUnsignedByte();
        int varArgs = in.readByte();
        targetObjectId = (varArgs & EFFECT_BIT_ID) != 0 ? readCompressedInt(in) : 0;

        if ((varArgs & EFFECT_BIT_POS1_X) != 0) {
            start = start.withX(in.readFloat());
        } else {
            start = start.withX(0);
        }

        if ((varArgs & EFFECT_BIT_POS1_Y) != 0) {
            start = start.withY(in.readFloat());
        } else {
            start = start.withY(0);
        }

        if ((varArgs & EFFECT_BIT_POS2_X) != 0) {
            end = end.withX(in.readFloat());
        } else {
            end = end.withX(0);
        }

        if ((varArgs & EFFECT_BIT_POS2_Y) != 0) {
            end = end.withY(in.readFloat());
        } else {
            end = end.withY(0);
        }

        if ((varArgs & EFFECT_BIT_COLOR) != 0) {
            color = in.readInt();
        } else {
            color = 0xFFFFFFFF;
        }

        if ((varArgs & EFFECT_BIT_DURATION) != 0) {
            duration = in.readFloat();
        } else {
            duration = 1;
        }
        if(varArgs >= 0){
            unknownByte = 100;
        }else{
            unknownByte = in.readByte();
        }
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
    }
}
