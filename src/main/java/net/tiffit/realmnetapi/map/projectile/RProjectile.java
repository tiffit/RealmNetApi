package net.tiffit.realmnetapi.map.projectile;

import net.tiffit.realmnetapi.api.Hooks;
import net.tiffit.realmnetapi.api.IObjectListener;
import net.tiffit.realmnetapi.api.event.EnemyHitEvent;
import net.tiffit.realmnetapi.api.event.EventHandler;
import net.tiffit.realmnetapi.assets.ConditionEffect;
import net.tiffit.realmnetapi.assets.xml.GameObject;
import net.tiffit.realmnetapi.assets.xml.Projectile;
import net.tiffit.realmnetapi.map.RMap;
import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.object.RObject;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.out.EnemyHitPacketOut;
import net.tiffit.realmnetapi.net.packet.out.OtherHitPacketOut;
import net.tiffit.realmnetapi.net.packet.out.PlayerHitPacketOut;
import net.tiffit.realmnetapi.util.Tuple;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.util.ArrayList;
import java.util.List;

public class RProjectile {

    private final RealmNetworker net;
    private final List<Integer> damageRecords = new ArrayList<>();
    private final IObjectListener<RProjectile> listener;
    private final ProjectileState state;
    private boolean dead;
    private final int id = nextProjId++;
    public int startTime = 0;

    private static int nextProjId = 0;

    public final float effectiveLifetime, effectiveSpeed;

    public ProjectileState getProjectileState() {
        return state;
    }

    private int getStartTime() {
        return startTime;
    }

    public void kill(){
        this.dead = true;
    }

    public boolean isDead(){
        return dead;
    }

    public int getProjectileId(){
        return id;
    }

    private RProjectile(RealmNetworker net, ProjectileState state){
        listener = Hooks.ProjectileListener.apply(this);
        this.net = net;
        this.state = state;
        this.effectiveLifetime = state.lifetimeMult * state.proj.lifetimeMS;
        this.effectiveSpeed = state.speedMult * state.proj.speed;
    }

    public static RProjectile create(RealmNetworker net, ProjectileState state){
        RProjectile proj = new RProjectile(net, state);
        proj.startTime = net.lastUpdate;//RealmNetworker.getTime();
        net.map.getProjectileList().add(proj);
        return proj;
    }

    public boolean update(int gameTime){
        boolean result = doUpdate(gameTime);
        if(result){
            listener.updateLoop();
        }else{
            listener.objectKill();
        }
        return result;
    }

    private boolean doUpdate(int gameTime){
        ProjectileState state = getProjectileState();
        int time = getStartTime();
        Vec2f vec = getPositionAt(state, gameTime);
        if (effectiveLifetime < gameTime - time){
            return false;
        }
        RMap map = net.map;
        if (!map.inMap(vec.x(), vec.y())) {
            if (state.team == ProjectileState.ProjectileTeam.ENEMY) {
                //net.send(new SquareHitPacketOut(gameTime, (byte) state.bulletId, state.ownerId));
            }
            return false;
        }
        RObject square = map.getStaticGameObject((int)vec.x(), (int)vec.y());
        if (square != null) {
            GameObjectState squareState = square.getState();
            GameObject squareGo = squareState.getGameObject();
            if ((!squareGo.enemy || !(state.team == ProjectileState.ProjectileTeam.ALLY || state.team == ProjectileState.ProjectileTeam.SELF))
                    && (squareGo.enemyOccupySquare || !state.proj.passesCover && squareGo.occupySquare)) {
                if (state.team == ProjectileState.ProjectileTeam.ENEMY) {
                    net.send(new OtherHitPacketOut(RealmNetworker.getTime(), (short) state.bulletId, state.ownerId, squareState.objectId));
                }
                return false;
            }
        }
        Tuple<Vec2f, GameObjectState> hit = getHit(state, vec);
        if (hit != null) {
            GameObjectState hitState = hit.b();
            GameObject hitGo = hitState.getGameObject();
            if (state.team == ProjectileState.ProjectileTeam.ENEMY || state.ownerId == map.getObjectId()) {
                if (hitState.objectId == map.getObjectId()) {
                    net.send(new PlayerHitPacketOut((short) state.bulletId, state.ownerId));
                    EventHandler.executeEvent(new EnemyHitEvent(this, hitState, false));
                } else if (hitGo.enemy) {
                    int damage = damageWithDefense(state.damage, hitState.getDefense(), state.proj.armorPierce, hitState);
                    boolean kill = hitState.getHP() <= damage;
                    net.send(new EnemyHitPacketOut(gameTime, (short) state.bulletId, map.getObjectId(), hitState.objectId, kill, map.getObjectId()));
                    EventHandler.executeEvent(new EnemyHitEvent(this, hitState, kill));
                    //RObject re = map.getEntityList().get(hitState.objectId);
                    //if(damage != 0)re.createDamageTextDark(damage);
                    //re.playDamage(kill);
//                    if (!kill){
//                        hitState.setStat(StatType.HP, hitState.getHP() - damage);
//                        map.getEntityList().get(hitState.objectId).mergeState(hitState);
//                    }else{
//                        hitState.setStat(StatType.HP, 1);
//                        map.getEntityList().get(hitState.objectId).mergeState(hitState);
//                    }
                } else if (!state.proj.multiHit) {
                    net.send(new OtherHitPacketOut(RealmNetworker.getTime(), (short) state.bulletId, state.ownerId, hit.b().objectId));
                }
            }
            if (state.proj.multiHit) {
                damageRecords.add(hit.b().objectId);
            } else {
                return false;
            }
        }
        return true;
    }

    private Tuple<Vec2f, GameObjectState> getHit(ProjectileState state, Vec2f pos) {
        RMap map = net.map;
        List<RObject> entities = map.getEntityList().getEntities();
        List<Tuple<Vec2f, GameObjectState>> rEntities = new ArrayList<>();
        for (RObject entity : entities) {
            if (!entity.isDead()) {
                rEntities.add(new Tuple<>(new Vec2f(entity.getCorrectedX(), entity.getCorrectedY()), entity.getState()));
            }
        }
        Vec2f playerPos = map.getPlayerPos().getPos();
        rEntities.add(0, new Tuple<>(new Vec2f(playerPos.x(), playerPos.y()), map.getSelfState()));

        Tuple<Vec2f, GameObjectState> closest = null;
        double closestDistance = Integer.MAX_VALUE;
        for (Tuple<Vec2f, GameObjectState> tuple : rEntities) {
            double entityX = tuple.a().x();
            double entityY = tuple.a().y();
            GameObjectState entityState = tuple.b();
            GameObject obj = entityState.getGameObject();
            if (obj == null) continue;
            if(obj.invincible || entityState.hasEffect(ConditionEffect.INVINCIBLE) || entityState.hasEffect(ConditionEffect.STASIS))continue;
            boolean isEnemy = (state.team == ProjectileState.ProjectileTeam.ALLY || state.team == ProjectileState.ProjectileTeam.SELF) && obj.enemy;
            boolean isPlayer = state.team == ProjectileState.ProjectileTeam.ENEMY && obj.player;
            if (isEnemy || isPlayer) {
                double dx = Math.abs(pos.x() - entityX);
                double dy = Math.abs(pos.y() - entityY);
                if (dx <= obj.radius && dy <= obj.radius) {
                    if (!state.proj.multiHit || !this.damageRecords.contains(entityState.objectId)) {
                        if (entityState.objectId == map.getObjectId()) {
                            return tuple;
                        }
                        double distance = dx * dx + dy * dy;
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closest = tuple;
                        }
                    }
                }
            }
        }
        return closest;
    }

    private int damageWithDefense(int damage, int defense, boolean armorPierce, GameObjectState hit) {
        if (armorPierce || hit.hasEffect(ConditionEffect.ARMORBROKEN)) defense = 0;
        else if (hit.hasEffect(ConditionEffect.ARMORED)) defense *= 2;
        if (hit.hasEffect(ConditionEffect.EXPOSED)) defense -= 20;
        damage = Math.max(damage * 3 / 20, damage - defense);
        if (hit.hasEffect(ConditionEffect.INVULNERABLE)) damage = 0;
        if (hit.hasEffect(ConditionEffect.PETRIFIED)) damage *= 0.7;
        if (hit.hasEffect(ConditionEffect.CURSE)) damage *= 1.2;
        return damage;
    }

    public Vec2f getPositionAt(ProjectileState state) {
        return getPositionAt(state, RealmNetworker.getTime());
    }

    public Vec2f getPositionAt(ProjectileState state, int currentTime) {
        int time = currentTime - getStartTime();
        Projectile proj = state.proj;
        Vec2f point = new Vec2f(state.startX, state.startY);
        double distance = CalculateDistance(time);
        double phase = state.bulletId % 2 == 0 ? 0 : Math.PI;
        if(proj.circleTurnAngle != 0 && proj.circleTurnDelay < time){
            distance = CalculateDistance(proj.circleTurnDelay);
            float angle = (float) Math.toRadians(proj.circleTurnAngle / proj.circleTurnDelay * (time - proj.circleTurnDelay));
            angle += state.angle;
            return point.add((float)(distance * Math.cos(angle)), (float)(distance * Math.sin(angle)));
        }else if (proj.wavy) {
            double sixPi = 6D * Math.PI;
            double piOver64 = Math.PI / 64D;
            double newAngle = state.angle + piOver64 * Math.sin(phase + sixPi * time / 1000);
            point = point.add((float)(distance * Math.cos(newAngle)), (float)(distance * Math.sin(newAngle)));
        } else if (proj.parametric) {
            double v1 = (double)time / effectiveLifetime * 2 * Math.PI;
            double v2 = Math.sin(v1) * (state.bulletId % 2 > 0 ? 1 : -1);
            double v3 = Math.sin(2 * v1) * (state.bulletId % 4 < 2 ? 1 : -1);
            double sinVal = Math.sin(state.angle);
            double cosVal = Math.cos(state.angle);
            point = point.add((float)((v2 * cosVal - v3 * sinVal) * proj.magnitude), (float)((v2 * sinVal + v3 * cosVal) * proj.magnitude));
        } else {
            if (proj.boomerang) {
                float halfwayTime = effectiveLifetime / 2f;
                if (time > halfwayTime) {
                    float halfwayDistance = CalculateDistance(halfwayTime);
                    distance = halfwayDistance*2 - distance;
                }
            }
            point = point.add((float)(distance * Math.cos(state.angle)), (float)(distance * Math.sin(state.angle)));
            if (proj.amplitude != 0) {
                double deflection = proj.amplitude * Math.sin(phase + time / effectiveLifetime * proj.frequency * 2 * Math.PI);
                point = point.add((float)(deflection * Math.cos(state.angle + Math.PI / 2)), (float)(deflection * Math.sin(state.angle + Math.PI / 2)));
            }
        }
        return point;
    }

    private float CalculateDistance(float time){
        Projectile proj = state.proj;
        if (proj.acceleration == 0 || proj.parametric) {
            return time * (effectiveSpeed / 10_000);
        }
        float preAccelerationTime = Math.min(time, proj.accelerationDelay);
        float distance = preAccelerationTime * (effectiveSpeed / 10_000);
        float clamp = proj.speedClamp;
        float clampTime = (clamp - effectiveSpeed) / proj.acceleration * 1000f + preAccelerationTime;
        if (clampTime < 0) clampTime = Float.MAX_VALUE;
        if (time > proj.accelerationDelay) {
            distance +=  (CalculateAccIntegral(Math.min(time, clampTime)/1000f) - CalculateAccIntegral(proj.accelerationDelay / 1000f)) / 10;
        }
        if (time > clampTime) {
            distance += (time-clampTime) * (clamp / 10_000);
        }
        return distance;
    }

    private float CalculateAccIntegral(float timeSeconds){
        Projectile proj = state.proj;
        float area = proj.acceleration * timeSeconds * 0.5f - proj.acceleration * proj.accelerationDelay / 1000f + effectiveSpeed;
        return area * timeSeconds;
    }

}
