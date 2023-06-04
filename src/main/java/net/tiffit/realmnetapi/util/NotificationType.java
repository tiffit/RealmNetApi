package net.tiffit.realmnetapi.util;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;

@RequiredArgsConstructor
public enum NotificationType {

    StatIncrease(0),
    ServerMessage(1),
    ErrorMessage(2),
    KeepMessage(3),
    Queue(5),
    Death(7),
    DungeonOpened(8),
    TeleportationError(9),
    DungeonCall(10);

    public final int id;

    private static HashMap<Integer, NotificationType> map = new HashMap<>();

    static {
        for (NotificationType value : NotificationType.values()) {
            map.put(value.id, value);
        }
    }

    public static NotificationType getNotificationType(int id){
        return map.getOrDefault(id, null);
    }
}
