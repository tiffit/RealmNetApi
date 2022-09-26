package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.assets.xml.Projectile;
import net.tiffit.realmnetapi.assets.xml.XMLLoader;
import net.tiffit.realmnetapi.map.object.RObject;
import net.tiffit.realmnetapi.map.object.RotMGEntityList;
import net.tiffit.realmnetapi.map.object.StatType;
import net.tiffit.realmnetapi.map.projectile.ProjectileState;
import net.tiffit.realmnetapi.map.projectile.RProjectile;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class EnemyShootPacketIn extends RotMGPacketIn {

    public short bulletId;
    public int ownerId;
    public int bulletType;
    public Vec2f startingPos;
    public float angle;
    public short damage;
    public int numShots;
    public float angleIncr;

    @Override
    public void read(DataInputStream in) throws IOException {
        bulletId = in.readShort();
        ownerId = in.readInt();
        bulletType = in.readUnsignedByte();
        startingPos = readWorldPosData(in);
        angle = in.readFloat();
        damage = in.readShort();
        if(in.available() > 0){
            numShots = in.readUnsignedByte();
            angleIncr = in.readFloat();
        }else{
            numShots = 1;
            angleIncr = 0;
        }
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        net.ackHandler.add(() -> {
            RotMGEntityList list = net.map.getEntityList();
            if(!list.has(ownerId) || list.get(ownerId).isDead()){
                net.ackHandler.addInvalidShoot();
                return;
            }
            RObject robj = list.get(ownerId);
            for (int i = 0; i < numShots; i++) {
                ProjectileState state = new ProjectileState();
                state.bulletId = bulletId + i;
                state.ownerType = robj.getGameObject().type;
                state.ownerId = ownerId;
                state.bulletType = bulletType;
                state.startX = startingPos.x();
                state.startY = startingPos.y();
                state.angle = angle + i * angleIncr;
                state.damage = damage;
                state.numShots = (byte)numShots;
                state.angleInc = angleIncr;
                state.team = ProjectileState.ProjectileTeam.ENEMY;
                if(robj.getState().hasStat(StatType.PROJECTILE_SPEED_MULT)){
                    state.speedMult = robj.getState().<Integer>getStat(StatType.PROJECTILE_SPEED_MULT) / 1000f;
                }
                if(robj.getState().hasStat(StatType.PROJECTILE_LIFE_MULT)){
                    state.lifetimeMult = robj.getState().<Integer>getStat(StatType.PROJECTILE_LIFE_MULT) / 1000f;
                }
                RObject shooter = list.get(ownerId);
                if (shooter != null) {
                    Projectile proj = null;
                    List<Projectile> projList = shooter.getGameObject().projectiles;
                    for (Projectile p : projList) {
                        if (p.id == bulletType) {
                            proj = p;
                            break;
                        }
                    }
                    if(proj == null && projList.size() > bulletType){
                        proj = projList.get(bulletType);
                    }
                    if (proj != null) {
                        state.proj = proj;
                        String objId = proj.objectId;
                        state.obj = XMLLoader.ID_TO_OBJECT.get(objId);
                        RProjectile.create(net, state);
                    } else {
                        System.out.println("Unknown bullet type " + bulletType + " for enemy " + shooter.getGameObject().id);
                    }
                }
            }
            net.ackHandler.addShoot();
        });
    }

    @Override
    public String getExtraInfo() {
        return "{" +
                "bulletId=" + bulletId +
                ", ownerId=" + ownerId +
                ", bulletType=" + bulletType +
                ", startingPos=" + startingPos +
                ", angle=" + angle +
                ", damage=" + damage +
                ", numShots=" + numShots +
                ", angleIncr=" + angleIncr +
                '}';
    }
}
