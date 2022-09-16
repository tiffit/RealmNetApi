package net.tiffit.realmnetapi.api.event;

import net.tiffit.realmnetapi.net.packet.in.ReconnectPacketIn;

public record ReconnectEvent(ReconnectPacketIn packet) {
}
