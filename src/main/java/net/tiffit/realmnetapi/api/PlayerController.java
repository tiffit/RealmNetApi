package net.tiffit.realmnetapi.api;

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

    public PlayerController(RealmNetworker net){
        this.net = net;
    }

    public void sendChatMessage(String text){
        net.send(new PlayerTextPacketOut(text));
    }

    public enum AbilityUseResult {
        SUCCESS,
        ON_COOLDOWN,
        NO_ABILITY
    }

    public AbilityUseResult useAbility(Vec2f pos){
        GameObjectState state = net.map.getSelfState();
        int equipItem = state.getStat(StatType.INVENTORY_1);
        if(equipItem == -1 || !XMLLoader.OBJECTS.containsKey(equipItem)){
            return AbilityUseResult.NO_ABILITY;
        }
        SlotObjectData data = new SlotObjectData(net.map.getObjectId(), 1, equipItem);
        UseItemPacketOut packet = new UseItemPacketOut(RealmNetworker.getTime(), data, pos, 1);
        net.send(packet);
        return AbilityUseResult.SUCCESS;
    }
}
