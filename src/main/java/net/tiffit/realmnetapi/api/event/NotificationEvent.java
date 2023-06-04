package net.tiffit.realmnetapi.api.event;

import net.tiffit.realmnetapi.net.packet.in.NotificationPacketIn;

public record NotificationEvent(NotificationPacketIn packet) {
}
