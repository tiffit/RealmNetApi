package net.tiffit.realmnetapi.api;

import net.tiffit.realmnetapi.assets.ConditionEffect;
import net.tiffit.realmnetapi.assets.xml.GameObject;
import net.tiffit.realmnetapi.assets.xml.XMLLoader;
import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.object.StatType;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.SlotObjectData;
import net.tiffit.realmnetapi.net.packet.out.PlayerTextPacketOut;
import net.tiffit.realmnetapi.net.packet.out.UseItemPacketOut;
import net.tiffit.realmnetapi.util.math.Vec2f;

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
        UseItemPacketOut packet = new UseItemPacketOut(time, data, pos, 1);
        net.send(packet);
        return AbilityUseResult.SUCCESS;
    }
}
