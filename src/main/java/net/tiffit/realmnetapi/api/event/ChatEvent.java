package net.tiffit.realmnetapi.api.event;

import net.tiffit.realmnetapi.net.packet.in.TextPacketIn;

public record ChatEvent(TextPacketIn packet) {
}
