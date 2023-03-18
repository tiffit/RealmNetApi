package net.tiffit.realmnetapi.api.event;

import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.object.StatType;

import java.util.Set;

public record PlayerDataEvent(GameObjectState state, Set<StatType> changedStats) {
}
