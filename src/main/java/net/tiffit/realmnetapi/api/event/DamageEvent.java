package net.tiffit.realmnetapi.api.event;

import net.tiffit.realmnetapi.net.packet.in.DamagePacketIn;

public record DamageEvent(DamagePacketIn packet) {
}
