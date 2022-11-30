package net.tiffit.realmnetapi.util;

import net.tiffit.realmnetapi.api.Hooks;
import net.tiffit.realmnetapi.api.IShootDecider;
import net.tiffit.realmnetapi.api.event.EventHandler;
import net.tiffit.realmnetapi.api.event.PlayerShootEvent;
import net.tiffit.realmnetapi.assets.ConditionEffect;
import net.tiffit.realmnetapi.assets.ItemType;
import net.tiffit.realmnetapi.assets.xml.GameObject;
import net.tiffit.realmnetapi.assets.xml.SubAttack;
import net.tiffit.realmnetapi.assets.xml.XMLLoader;
import net.tiffit.realmnetapi.map.RMap;
import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.object.RObject;
import net.tiffit.realmnetapi.map.object.StatType;
import net.tiffit.realmnetapi.map.projectile.ProjectileState;
import net.tiffit.realmnetapi.map.projectile.RProjectile;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.out.PlayerShootPacketOut;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.util.HashMap;
import java.util.List;

public class Updater implements Runnable {



    private RealmNetworker net;

    public HashMap<Integer, AttackTracker> attacks = new HashMap<>();
    public int current_item = 0;
    public int next_bullet_id = 1;

    public Updater(RealmNetworker net){
        this.net = net;
    }

    @Override
    public void run() {
        while(net.connected) {
            RealmNetworker.updateTime();
            if(net.map != null && net.map.getPlayerPos() != null) {
                //net.logger.write("packet", "Update Start - " + RealmNetworker.getTime());

                //Update Entities
                try {
                    net.map.getEntityList().getAll(rEntity -> true).forEach(RObject::updateLoop);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                //Update Projectiles
                try {
                    updateProjectiles();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                //Shoot
                try {
                    attemptShoot();
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                //Move Records
                net.records.lock.lock();
                Vec2f pos = net.map.getPlayerPos().getPos();
                net.records.addRecord(RealmNetworker.getTime(), pos.x(), pos.y());
                net.records.lock.unlock();

                net.lastUpdate = RealmNetworker.getTime();

                net.ackHandler.process();

                //Create Projectiles
                try {
                    createProjectiles();
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                //net.logger.write("packet", "Update End - " + RealmNetworker.getTime());
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void createProjectiles(){
//        ProjectileState[] states;
//        while((states = net.ackHandler.pollPending()) != null){
//            for (ProjectileState state : states) {
//                if(state != null){
//                    RProjectile.create(net, state);
//                }
//            }
//        }
    }

    private void updateProjectiles(){
        List<RProjectile> projectiles = net.map.getProjectileList().getProjectiles();
        for(RProjectile proj : projectiles) {
            if (proj.isDead() || net.map.getProjectileList().isRemoved(proj.getProjectileId())){
                continue;
            }
            if (!proj.update(RealmNetworker.getTime())) {
                proj.kill();
                net.map.getProjectileList().remove(proj.getProjectileId());
            }
        }
    }

    private void attemptShoot(){
        IShootDecider decider = Hooks.ShootDecider;
        if(decider != null && decider.shouldShoot()){
            GameObjectState playerState = net.map.getSelfState();
            if(playerState.hasEffect(ConditionEffect.STUNNED) || playerState.hasEffect(ConditionEffect.PETRIFIED))return;
            int heldStat = playerState.getStat(StatType.INVENTORY_0);
            if(heldStat > 0){
                GameObject go = XMLLoader.OBJECTS.get(heldStat);
                ItemType type = ItemType.byID(go.slotType);
                if(type == ItemType.SWORD_TYPE || type == ItemType.DAGGER_TYPE || type == ItemType.BOW_TYPE || type == ItemType.WAND_TYPE || type == ItemType.STAFF_TYPE || type == ItemType.KATANA_TYPE){
                    if(heldStat != current_item){
                        if(go.subAttacks.isEmpty()){
                            attacks.put(0, new AttackTracker().noSubAttacks(go));
                        }else{
                            for (SubAttack subAttack : go.subAttacks) {
                                AttackTracker tracker = new AttackTracker();
                                tracker.attack = subAttack;
                                attacks.put(subAttack.index, tracker);
                            }
                        }
                        current_item = heldStat;
                    }
                    float arcGap = (float) Math.toRadians(go.arcGap);
                    float angle = decider.getAngleRads(go, arcGap);
                    attacks.values().forEach(attackTracker -> attackTracker.attack(angle, net, go, type, playerState));
                }
            }
        }
    }

    private double attackFrequency(GameObjectState state) {
        if(state.hasEffect(ConditionEffect.DAZED)){
            return RConstants.MIN_ATTACK_FREQ;
        }
        double freq = RConstants.MIN_ATTACK_FREQ + state.getDexterity() / 75.0f * (RConstants.MAX_ATTACK_FREQ - RConstants.MIN_ATTACK_FREQ);
        if(state.hasEffect(ConditionEffect.BERSERK)){
            freq *= 1.25;
        }
        return freq;
    }

    private int getBulletId() {
        int bullet = next_bullet_id;
        next_bullet_id = (next_bullet_id + 1) % 256;
        return bullet;
    }

    private double attackMultiplier(GameObjectState state) {
        if(state.hasEffect(ConditionEffect.WEAK)){
            return RConstants.MIN_ATTACK_MULT;
        }
        double mult = RConstants.MIN_ATTACK_MULT + state.getAttack() / 75d * (RConstants.MAX_ATTACK_MULT - RConstants.MIN_ATTACK_MULT);
        if(state.hasEffect(ConditionEffect.DAMAGING)){
            mult *= 1.25;
        }
        return mult;
    }

    private class AttackTracker {
        public int attack_period = 0;
        public long attack_start = 0;
        public int burstCount = 0;
        public SubAttack attack;

        public AttackTracker noSubAttacks(GameObject go){
            attack = new SubAttack();
            attack.index = -1;
            attack.numProjectiles = go.numProjectiles;
            attack.projectileId = 0;
            attack.rateOfFire = go.rateOfFire;
            attack.defaultAngleDeg = 0;
            attack.posOffset = Vec2f.ZERO;
            return this;
        }

        public void attack(float angle, RealmNetworker net, GameObject go, ItemType type, GameObjectState playerState){
            attack_period = (int)Math.ceil((1 / attackFrequency(playerState) * (1 / attack.rateOfFire)));
            int ms = RealmNetworker.getTime();
            if (ms <= attack_start + attack_period) {
                return;
            }
            if(attack.burstCount > 0){
                if(burstCount == 0) {
                    float dexScalar = Math.min(75f, playerState.getDexterity()) / 75f;
                    float burstDelay = attack.burstDelay - dexScalar * (attack.burstDelay - attack.burstMinDelay);
                    if (ms <= attack_start + burstDelay) {
                        return;
                    }
                    burstCount = attack.burstCount;
                }else{
                    burstCount--;
                }
            }
            angle += (float)Math.toRadians(attack.defaultAngleDeg);
            attack_start = ms;
            RMap map = net.map;
            float arcGap = (float) Math.toRadians(go.arcGap);
            Vec2f shootPos = map.getPlayerPos().getPos();
            if(!attack.posOffset.isZero()){
                double offsetAngle = Math.atan2(attack.posOffset.x(), attack.posOffset.y()) + angle;
                float magnitude = (float) Math.sqrt(attack.posOffset.distanceSqr(Vec2f.ZERO));
                shootPos = shootPos.add(new Vec2f((float) Math.cos(offsetAngle) * magnitude, (float) Math.sin(offsetAngle) * magnitude));
            }
            shootPos = shootPos.add(Vec2f.rotate(0.3f, angle));

            for (int i = 0; i < attack.numProjectiles; i++) {
                short bulletId = (short) getBulletId();
                ProjectileState state = new ProjectileState();
                state.ownerId = map.getObjectId();
                state.bulletId = bulletId;
                state.team = ProjectileState.ProjectileTeam.SELF;
                state.angle = angle + arcGap * i;
                state.startX = shootPos.x();
                state.startY = shootPos.y();
                state.proj = go.projectiles.get(0);
                state.numShots = (byte) attack.numProjectiles;
                state.angleInc = arcGap;
                state.damage = (short) (attackMultiplier(playerState) * map.getRandom().nextIntRange(state.proj.minDamage, state.proj.maxDamage));
                String objId = state.proj.objectId;
                state.obj = XMLLoader.ID_TO_OBJECT.getOrDefault(objId, null);

                RProjectile proj = RProjectile.create(net, state);
                proj.startTime = ms;
                PlayerShootPacketOut packet = new PlayerShootPacketOut(ms, bulletId, (short)go.type, (byte)attack.index, shootPos, angle, false);
                net.send(packet);
            }
            EventHandler.executeEvent(new PlayerShootEvent(angle, go, type));
        }
    }

}
