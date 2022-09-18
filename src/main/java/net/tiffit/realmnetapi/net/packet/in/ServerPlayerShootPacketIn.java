package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.assets.xml.GameObject;
import net.tiffit.realmnetapi.assets.xml.Projectile;
import net.tiffit.realmnetapi.assets.xml.XMLLoader;
import net.tiffit.realmnetapi.map.RMap;
import net.tiffit.realmnetapi.map.object.RotMGEntityList;
import net.tiffit.realmnetapi.map.projectile.ProjectileState;
import net.tiffit.realmnetapi.map.projectile.RProjectile;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;
import net.tiffit.realmnetapi.util.math.Vec2f;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class ServerPlayerShootPacketIn extends RotMGPacketIn {

    public short bulletId;
    public int ownerId;
    public int containerType;
    public Vec2f startingPos;
    public float angle;
    public short damage;
    public int unknown;
    public byte unknown2;
    public int bulletCount;
    public float angleBetween;

    @Override
    public void read(DataInputStream in) throws IOException {
        bulletId = in.readShort();
        ownerId = in.readInt();
        containerType = in.readInt();
        startingPos = readWorldPosData(in);
        angle = in.readFloat();
        damage = in.readShort();
        unknown = in.readInt();
        unknown2 = in.readByte();
        if(in.available() > 0){
            bulletCount = in.readByte();
            angleBetween = in.readFloat();
        }else{
            bulletCount = 1;
            angleBetween = 0f;
        }
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        net.ackHandler.add(() -> {
            RMap map = net.map;
            RotMGEntityList list = map.getEntityList();
            boolean self = map.getObjectId() == ownerId;
            for (int i = 0; i < bulletCount; i++) {
                ProjectileState state = new ProjectileState();
                state.bulletId = bulletId + i;
                state.ownerId = ownerId;
                state.bulletType = unknown;
                state.startX = startingPos.x();
                state.startY = startingPos.y();
                state.angle = angle + i * angleBetween;
                state.damage = damage;
                state.numShots = (byte)bulletCount;
                state.angleInc = angleBetween;
                state.team = self ? ProjectileState.ProjectileTeam.SELF : ProjectileState.ProjectileTeam.ALLY;

                GameObject item = XMLLoader.OBJECTS.get(containerType);
                if (item != null) {
                    Projectile proj = null;
                    List<Projectile> projList = item.projectiles;
                    for (Projectile p : projList) {
                        if (p.id == state.bulletType) {
                            proj = p;
                            break;
                        }
                    }
                    if(proj == null && projList.size() > state.bulletType){
                        proj = projList.get(state.bulletType);
                    }
                    if (proj != null) {
                        state.proj = proj;
                        String objId = proj.objectId;
                        state.obj = XMLLoader.ID_TO_OBJECT.get(objId);
                        RProjectile.create(net, state);
                    } else {
                        System.out.println("Unknown bullet type " + state.bulletType + " for item " + item.id);
                    }
                }
            }
            if(self){
                net.ackHandler.addShoot();
            }
        });
    }
}
