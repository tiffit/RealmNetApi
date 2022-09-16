package net.tiffit.realmnetapi.api.event;

import net.tiffit.realmnetapi.map.object.GameObjectState;

public record PlayerDataEvent(GameObjectState state) {
}
