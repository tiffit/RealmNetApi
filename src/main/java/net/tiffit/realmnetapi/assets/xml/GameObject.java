package net.tiffit.realmnetapi.assets.xml;

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

    public boolean staticObject;
    public boolean occupySquare;
    public boolean fullOccupy;
    public boolean enemyOccupySquare;
    public boolean blocksSight;
    public boolean invincible;
    public HealthBarBoss healthBar = HealthBarBoss.NONE;

    public int maxHitPoints;

    public float radius;

    public boolean intergamePortal;
    public String dungeonName;

    public List<Projectile> projectiles = new ArrayList<>();

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

    public String hitSound;
    public String deathSound;


    public Map<Integer, Style> styles = new HashMap<>();

}
