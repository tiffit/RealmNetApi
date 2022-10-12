package net.tiffit.realmnetapi.api.event;

import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.projectile.RProjectile;

public record EnemyHitEvent(RProjectile proj, GameObjectState enemyState, int damage, boolean kill){
}
