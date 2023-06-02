package net.tiffit.realmnetapi.api;

import net.tiffit.realmnetapi.assets.ConditionEffect;
import net.tiffit.realmnetapi.assets.ItemType;
import net.tiffit.realmnetapi.assets.xml.GameObject;
import net.tiffit.realmnetapi.assets.xml.ItemActivate;
import net.tiffit.realmnetapi.assets.xml.XMLLoader;
import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.object.RObject;
import net.tiffit.realmnetapi.map.object.StatType;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.SlotObjectData;
import net.tiffit.realmnetapi.net.packet.out.PlayerTextPacketOut;
import net.tiffit.realmnetapi.net.packet.out.TeleportPacketOut;
import net.tiffit.realmnetapi.net.packet.out.UseItemPacketOut;
import net.tiffit.realmnetapi.util.Updater;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.util.HashMap;

public class PlayerController {
    private RealmNetworker net;
    private int lastAbilityUse;

    public PlayerController(RealmNetworker net){
        this.net = net;
    }

    public void sendChatMessage(String text){
        net.send(new PlayerTextPacketOut(text));
    }

    public enum AbilityUseResult {
        SUCCESS,
        ON_COOLDOWN,
        NO_ABILITY,
        NOT_USABLE,
        NOT_ENOUGH_MANA,
        SILENCED
    }

    public AbilityUseResult useAbility(Vec2f pos){
        GameObjectState state = net.map.getSelfState();
        int equipItem = state.getStat(StatType.INVENTORY_1);
        if(state.hasEffect(ConditionEffect.SILENCED)){
            return AbilityUseResult.SILENCED;
        }
        if(equipItem == -1 || !XMLLoader.OBJECTS.containsKey(equipItem)){
            return AbilityUseResult.NO_ABILITY;
        }
        GameObject item = XMLLoader.OBJECTS.get(equipItem);
        if(!item.usable){
            return AbilityUseResult.NOT_USABLE;
        }
        if(item.mpCost > state.<Integer>getStat(StatType.MP)){
            return AbilityUseResult.NOT_ENOUGH_MANA;
        }
        int time = RealmNetworker.getTime();
        if(time - lastAbilityUse < item.cooldown * 1000){
            return AbilityUseResult.ON_COOLDOWN;
        }
        lastAbilityUse = time;
        SlotObjectData data = new SlotObjectData(net.map.getObjectId(), 1, equipItem);
        net.send(new UseItemPacketOut(time, data, pos, 1));

        if(item.activate == ItemActivate.Shoot){
            Updater updater = net.updater;
            IShootDecider shootDecider = net.hooks.ShootDecider;
            HashMap<Integer, Updater.AttackTracker> subAttacks = updater.createSubAttacks(item);
            updater.runSubAttacks(subAttacks, shootDecider, item, ItemType.byID(item.slotType), net.map.getSelfState(), true);
        }

        return AbilityUseResult.SUCCESS;
    }

    public void teleport(RObject obj){
        GameObjectState state = obj.getState();
        if(state.hasStat(StatType.NAME)){
            net.send(new TeleportPacketOut(state.objectId, state.getStat(StatType.NAME)));
        }
    }
}
