package net.tiffit.realmnetapi.api.event;

import net.tiffit.realmnetapi.assets.xml.GameObject;

public record PlayerShootEvent(float angle, GameObject weapon){
}
