package net.tiffit.realmnetapi.assets;

import java.io.Serializable;

public enum ConditionEffect implements Serializable {
    NOTHING(0),
    DEAD(1, "Dead", false),
    QUIET(2, "Quiet", false),
    WEAK(3, "Weak", false),
    SLOWED(4, "Slowed", false),
    SICK(5, "Sick", false),
    DAZED(6, "Dazed", false),
    STUNNED(7, "Stunned", false),
    BLIND(8, "Blind", false),
    HALLUCINATING(9, "Hallucinating", false),
    DRUNK(10, "Drunk", false),
    CONFUSED(11, "Confused", false),
    STUN_IMMUNE(12, "Stun Immune", true),
    INVISIBLE(13, "Invisible", true),
    PARALYZED(14, "Paralyzed", false),
    SPEEDY(15, "Speedy", true),
    BLEEDING(16, "Bleeding", false),
    ARMORBROKENIMMUNE(17, "Armorbreak Immune", true),
    HEALING(18, "Healing", true),
    DAMAGING(19, "Damaging", true),
    BERSERK(20, "Berserk", true),
    IN_COMBAT(21, "In Combat", true),
    STASIS(22, "Stasis", false),
    STASIS_IMMUNE(23, "Stasis Immune", false),
    INVINCIBLE(24, "Invincible", true),
    INVULNERABLE(25, "Invulnerable", true),
    ARMORED(26, "Armored", true),
    ARMORBROKEN(27, "Armor Broken", false),
    HEXED(28, "Hexed", false),
    NINJA_SPEEDY(29, "Ninja Speedy", true),
    UNSTABLE(30, "Unstable", false),
    DARKNESS(31, "Invincible", false),
    SLOWED_IMMUNE(32, "Slowed Immune", true, true),
    DAZED_IMMUNE(33, "Dazed Immune", true, true),
    PARALYZED_IMMUNE(34, "Paralyze Immune", true,  true),
    PETRIFIED(35, "Petrified", false,  true),
    PETRIFIED_IMMUNE(36, "Petrified Immune", false,  true),
    PET_EFFECT_ICON(37, "Pet Effect", false,  true),
    CURSE(38, "Curse", false,  true),
    CURSE_IMMUNE(39, "Curse Immune", true,  true),
    HP_BOOST(40, "HP+", true,  true),
    MP_BOOST(41, "MP+", true,  true),
    ATT_BOOST(42, "ATT+", true,  true),
    DEF_BOOST(43, "DEF+", true,  true),
    SPD_BOOST(44, "SPD+", true,  true),
    VIT_BOOST(45, "VIT+", true,  true),
    WIS_BOOST(46, "WIS+", true,  true),
    DEX_BOOST(47, "DEX+", true,  true),
    SILENCED(48, "Silenced", false,  true),
    EXPOSED(49, "Exposed", false,  true),
    ENERGIZED(50, "Energized", true,  true),
    HP_DEBUFF(51, "HP-", false,  true),
    MP_DEBUFF(52, "MP-", false,  true),
    ATT_DEBUFF(53, "ATT-", false,  true),
    DEF_DEBUFF(54, "DEF-", false,  true),
    SPD_DEBUFF(55, "SPD-", false,  true),
    VIT_DEBUFF(56, "VIT-", false,  true),
    WIS_DEBUFF(57, "WIS-", false,  true),
    DEX_DEBUFF(58, "DEX-", false,  true),
    GROUND_DAMAGE(99);

    public final int id;
    public final boolean newCondition;
    public final boolean buff;
    public final String displayName;

    ConditionEffect(int id){
        this(id, "", true, false);
    }

    ConditionEffect(int id, String displayName, boolean buff){
        this(id, displayName, buff, false);
    }
    ConditionEffect(int id, String displayName, boolean buff, boolean newCondition){
        this.id = id;
        this.newCondition = newCondition;
        this.buff = buff;
        this.displayName = displayName;
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
            if (effect.name().equalsIgnoreCase(name)) return effect;
        }
        return null;
    }

    public String getDisplayName() {
        return displayName.isEmpty() ? name() : displayName;
    }
}
