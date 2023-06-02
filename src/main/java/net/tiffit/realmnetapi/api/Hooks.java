package net.tiffit.realmnetapi.api;

import net.tiffit.realmnetapi.map.RMap;
import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.object.RObject;
import net.tiffit.realmnetapi.map.projectile.RProjectile;
import net.tiffit.realmnetapi.net.packet.in.MapInfoPacketIn;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Hooks {

    public Supplier<IPlayerPosTracker> PlayerPosTracker = IPlayerPosTracker.BasicPlayerPosTracker::new;
    public BiFunction<GameObjectState, RMap, RObject> RObjectFunc = RObject::new;
    public Function<RObject, IObjectListener<RObject>> ObjectListener = IObjectListener.EmptyObjectListener::new;
    public Function<RProjectile, IObjectListener<RProjectile>> ProjectileListener = IObjectListener.EmptyObjectListener::new;
    public IShootDecider ShootDecider = new IShootDecider.ShootDecider();
    public MapInfoPacketIn.ICharacterPacketFactory CharacterFactory = null;
}
