package net.tiffit.realmnetapi.api.event;

import net.tiffit.realmnetapi.assets.xml.Ground;
import net.tiffit.realmnetapi.util.math.Vec2f;

public record GroundDamageEvent(Ground tile, Vec2f pos) {
}
