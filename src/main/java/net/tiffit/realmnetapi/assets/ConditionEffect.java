package net.tiffit.realmnetapi.assets;

import java.io.Serializable;

public enum ConditionEffect implements Serializable {
    NOTHING(0),
    DEAD(1),
    QUIET(2),
    WEAK(3),
    SLOWED(4),
    SICK(5),
    DAZED(6),
    STUNNED(7),
    BLIND(8),
    HALLUCINATING(9),
    DRUNK(10),
    CONFUSED(11),
    STUN_IMMUNE(12),
    INVISIBLE(13),
    PARALYZED(14),
    SPEEDY(15),
    BLEEDING(16),
    ARMORBROKENIMMUNE(17),
    HEALING(18),
    DAMAGING(19),
    BERSERK(20),
    PAUSED(21),
    STASIS(22),
    STASIS_IMMUNE(23),
    INVINCIBLE(24),
    INVULNERABLE(25),
    ARMORED(26),
    ARMORBROKEN(27),
    HEXED(28),
    NINJA_SPEEDY(29),
    UNSTABLE(30),
    DARKNESS(31),
    SLOWED_IMMUNE(32, true),
    DAZED_IMMUNE(33, true),
    PARALYZED_IMMUNE(34, true),
    PETRIFIED(35, true),
    PETRIFIED_IMMUNE(36, true),
    PET_EFFECT_ICON(37, true),
    CURSE(38, true),
    CURSE_IMMUNE(39, true),
    HP_BOOST(40, true),
    MP_BOOST(41, true),
    ATT_BOOST(42, true),
    DEF_BOOST(43, true),
    SPD_BOOST(44, true),
    VIT_BOOST(45, true),
    WIS_BOOST(46, true),
    DEX_BOOST(47, true),
    SILENCED(48, true),
    EXPOSED(49, true),
    ENERGIZED(50, true),
    HP_DEBUFF(51, true),
    MP_DEBUFF(52, true),
    ATT_DEBUFF(53, true),
    DEF_DEBUFF(54, true),
    SPD_DEBUFF(55, true),
    VIT_DEBUFF(56, true),
    WIS_DEBUFF(57, true),
    DEX_DEBUFF(58, true),
    GROUND_DAMAGE(99);

    public final int id;
    public final boolean newCondition;

    ConditionEffect(int id){
        this(id, false);
    }

    ConditionEffect(int id, boolean newCondition){
        this.id = id;
        this.newCondition = newCondition;
    }

    public int getConditionBit(){
        return 1 << id - (newCondition ? 32 : 1);
    }

    public static ConditionEffect byId(int id){
        for(ConditionEffect effect : values()){
            if(effect.id == id)return effect;
        }
        return null;
    }

    public static ConditionEffect byName(String name){
        for(ConditionEffect effect : values()) {
            if (effect.name().toLowerCase().equals(name.toLowerCase())) return effect;
        }
        return null;
    }

}
