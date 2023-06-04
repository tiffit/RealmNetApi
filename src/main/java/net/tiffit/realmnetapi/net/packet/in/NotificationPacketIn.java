package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.api.event.NotificationEvent;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;
import net.tiffit.realmnetapi.util.NotificationType;

import java.io.DataInputStream;
import java.io.IOException;

public class NotificationPacketIn extends RotMGPacketIn {

    public NotificationType type;
    private int notifId;
    public int extra;
    public String message;
    public int objectId;
    public short queuePos;
    public short starCount;

    public void read(DataInputStream in) throws IOException {
        notifId = in.readByte();
        type = NotificationType.getNotificationType(notifId);
        extra = in.readByte();
        if(type == null)return;
        switch (type){
            case TeleportationError -> {
                message = in.readUTF();
            }
            case Queue -> {
                objectId = in.readInt();
                queuePos = in.readShort();
            }
            case DungeonCall -> {
                message = in.readUTF();
                objectId = in.readInt();
                starCount = in.readShort();
            }
        }
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        if(type != null){
            net.eventHandler.executeEvent(new NotificationEvent(this));
        }else{
            net.logger.write("Unknown notification type " + notifId);
        }
    }

    @Override
    public String toString() {
        return "{type=" + type + '}';
    }
}
