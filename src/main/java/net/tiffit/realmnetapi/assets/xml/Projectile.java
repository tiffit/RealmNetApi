package net.tiffit.realmnetapi.assets.xml;

import net.tiffit.realmnetapi.assets.ConditionEffect;

import java.io.Serializable;
import java.util.HashMap;

public class Projectile implements Serializable {
    public int id = -1;
    public String objectId;

    public int damage, minDamage, maxDamage;
    public float speed;
    public float lifetimeMS;
    public int size = 100;

    public HashMap<ConditionEffect, Float> effects = new HashMap<>();

    public boolean parametric, faceDir, passesCover, multiHit,
            armorPierce, boomerang, wavy;

    public float magnitude = 3, amplitude = 0, frequency = 1;

}
