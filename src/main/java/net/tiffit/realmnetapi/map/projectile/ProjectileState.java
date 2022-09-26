package net.tiffit.realmnetapi.map.projectile;

import net.tiffit.realmnetapi.assets.xml.GameObject;
import net.tiffit.realmnetapi.assets.xml.Projectile;

public class ProjectileState {

    public GameObject obj;
    public Projectile proj;
    public ProjectileTeam team;
    public int bulletId;
    public int ownerType;
    public int ownerId;
    public int bulletType;
    public float startX;
    public float startY;
    public double angle;
    public short damage;
    public byte numShots;
    public double angleInc;
    public float lifetimeMult = 1, speedMult = 1;

    public enum ProjectileTeam{
        ENEMY, SELF, ALLY
    }

}
