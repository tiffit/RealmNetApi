package net.tiffit.realmnetapi.api.event;

import net.tiffit.realmnetapi.net.packet.in.DeathPacketIn;

public record DeathEvent(DeathPacketIn packet) {
}
