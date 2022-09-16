package net.tiffit.realmnetapi.api.event;

import net.tiffit.realmnetapi.assets.xml.Ground;
import net.tiffit.realmnetapi.map.RMap;
import net.tiffit.realmnetapi.util.math.Vec2i;

import java.util.HashMap;

public record TileAddEvent(RMap map, HashMap<Vec2i, Ground> newTiles) {
}
