package net.tiffit.realmnetapi.map.object;

import net.tiffit.realmnetapi.assets.ConditionEffect;
import net.tiffit.realmnetapi.assets.xml.GameObject;
import net.tiffit.realmnetapi.assets.xml.XMLLoader;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GameObjectState {

    public int type;
    public int objectId;
    public Vec2f position;
    private GameObject go;
    public HashMap<StatType, Integer> STATS_INT = new HashMap<>();
    public HashMap<StatType, String> STATS_STR = new HashMap<>();

    public GameObjectState(int type, int objectId, Vec2f pos){
        this.type = type;
        this.objectId = objectId;
        this.position = pos;
    }

    public GameObjectState merge(GameObjectState state){
        if(state.type >= 0)this.type = state.type;
        this.objectId = state.objectId;
        this.position = state.position;
        STATS_INT.putAll(state.STATS_INT);
        STATS_STR.putAll(state.STATS_STR);
        return this;
    }

    public void setStat(StatType type, int val){
        STATS_INT.put(type, val);
    }

    public void setStat(StatType type, String val){
        STATS_STR.put(type, val);
    }

    public boolean hasStat(StatType stat){
        if(stat.stringType)return STATS_STR.containsKey(stat);
        return STATS_INT.containsKey(stat);
    }

    public <T> T getStat(StatType stat){
        if(stat.stringType)return (T) STATS_STR.get(stat);
        return (T) STATS_INT.get(stat);
    }

    public GameObject getGameObject(){
        if(go == null)go = XMLLoader.OBJECTS.get(type);
        return go;
    }

    public int getDefense(){
        return getStat(StatType.DEFENSE);
    }

    public int getAttack(){
        return getStat(StatType.ATTACK);
    }

    public int getSpeed(){
        return getStat(StatType.SPEED);
    }

    public int getVitality(){
        return getStat(StatType.VITALITY);
    }

    public int getWisdom(){
        return getStat(StatType.WISDOM);
    }

    public int getDexterity(){
        return getStat(StatType.DEXTERITY);
    }

    public int getHP(){
        if(!hasStat(StatType.HP))return 1;
        return getStat(StatType.HP);
    }

    public int getMP(){
        return getStat(StatType.MP);
    }

    public int getHPMax(){
        int currentHp = getHP();
        try {
            if (!hasStat(StatType.MAX_HP)) {
                setStat(StatType.MAX_HP, Math.max(getGameObject().maxHitPoints, currentHp));
            }
        }catch(NullPointerException ex){
            ex.printStackTrace();
            System.err.println("Unknown object: " + type);
            return 1;
        }
        int maxHp = getStat(StatType.MAX_HP);
        if(currentHp > maxHp){
            setStat(StatType.MAX_HP, currentHp);
            return currentHp;
        }
        return maxHp;
    }

    public int getMPMax(){
        return getStat(StatType.MAX_MP);
    }

    public ArrayList<ConditionEffect> getAllEffects(){
        ArrayList<ConditionEffect> list = new ArrayList();
        for(ConditionEffect effect : ConditionEffect.values()){
            if(hasEffect(effect))list.add(effect);
        }
        return list;
    }

    public boolean hasEffect(ConditionEffect effect){
        int condStat = 0;
        if(effect.newCondition){
            if(hasStat(StatType.NEW_CON))condStat = this.<Integer>getStat(StatType.NEW_CON);
        }else{
            if(hasStat(StatType.CONDITION))condStat = this.<Integer>getStat(StatType.CONDITION);
        }
        return (condStat & effect.getConditionBit()) != 0;
    }

    public static GameObjectState read(boolean getType, DataInput in) throws IOException {
        int type = getType ? in.readUnsignedShort() : -1;
        int objectId = RotMGPacketIn.readCompressedInt(in);
        Vec2f pos = RotMGPacketIn.readWorldPosData(in);
        GameObjectState state = new GameObjectState(type, objectId, pos);
        int statCount = RotMGPacketIn.readCompressedInt(in);
        for (int j = 0; j < statCount; j++) {
            int statTypeByte = in.readUnsignedByte();
            StatType statType = StatType.byID(statTypeByte);
            if(statType == null){
                System.out.println("Unknown stat " + statTypeByte);
                RotMGPacketIn.readCompressedInt(in);
                RotMGPacketIn.readCompressedInt(in);
                continue;
            }
            if(statType.stringType) {
                state.setStat(statType, in.readUTF());
            } else {
                state.setStat(statType, RotMGPacketIn.readCompressedInt(in));
            }
            int extra = RotMGPacketIn.readCompressedInt(in);
        }
        return state;
    }
}
