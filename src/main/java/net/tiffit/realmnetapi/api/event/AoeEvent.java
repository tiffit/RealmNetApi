package net.tiffit.realmnetapi.api.event;

import net.tiffit.realmnetapi.net.packet.in.AoePacketIn;

public record AoeEvent(AoePacketIn packet) {
}
