package net.tiffit.realmnetapi.assets.xml;

import net.tiffit.realmnetapi.map.object.StatType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameObject implements Serializable {

    public int type;
    public String id;
    public String setName = "";

    public String displayid;
    public String description;
    public String goClass;
    public int size;
    public boolean enemy;
    public boolean player;

    public List<Texture> texture = new ArrayList<>();
    public Map<Integer, Texture> altTextures = new HashMap<>();
    public Texture textureTop;
    public String model;
    public Map<String, Animation> animations = new HashMap<>();
    public int color;
    public int rotation;
    public float angleCorrection;
    public boolean drawOnGround;
    public boolean noTexture;
    public String defaultSkin;
    public boolean alwaysPositiveHealth;

    public boolean staticObject;
    public boolean occupySquare;
    public boolean fullOccupy;
    public boolean enemyOccupySquare;
    public boolean blocksSight;
    public boolean protectFromGroundDamage;
    public boolean protectFromSink;
    public boolean invincible;
    public HealthBarBoss healthBar = HealthBarBoss.NONE;

    public int maxHitPoints;

    public float radius;

    public boolean intergamePortal;
    public String dungeonName;

    public List<SubAttack> subAttacks = new ArrayList<>();
    public List<Projectile> projectiles = new ArrayList<>();

    public Map<Integer, Integer> unlockLevels = new HashMap<>();

    public HashMap<StatType, Integer> maxStats = new HashMap<>();

    public boolean item;
    public int tier;
    public int quantity;
    public int slotType;
    public float rateOfFire;
    public int bagType;
    public int numProjectiles;
    public float arcGap;
    public boolean soulbound;
    public boolean consumable;
    public boolean potion;
    public int mpCost;
    public boolean usable;
    public float cooldown;
    public ItemActivate activate;

    public String hitSound;
    public String deathSound;
    public String sound;


    public Map<Integer, Style> styles = new HashMap<>();

    public String getEffectiveName(){
        return displayid.isEmpty() ? id : displayid;
    }

}
