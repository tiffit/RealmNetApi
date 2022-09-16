package net.tiffit.realmnetapi.api;

import net.tiffit.realmnetapi.map.RMap;
import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.object.RObject;
import net.tiffit.realmnetapi.map.projectile.RProjectile;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Hooks {

    public static Supplier<IPlayerPosTracker> PlayerPosTracker = IPlayerPosTracker.BasicPlayerPosTracker::new;
    public static BiFunction<GameObjectState, RMap, RObject> RObjectFunc = RObject::new;
    public static Function<RObject, IObjectListener<RObject>> ObjectListener = IObjectListener.EmptyObjectListener::new;
    public static Function<RProjectile, IObjectListener<RProjectile>> ProjectileListener = IObjectListener.EmptyObjectListener::new;
    public static IShootDecider ShootDecider = new IShootDecider.ShootDecider();
}
