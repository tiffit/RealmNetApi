package net.tiffit.realmnetapi.net.packet.in;

import net.tiffit.realmnetapi.assets.xml.GameObject;
import net.tiffit.realmnetapi.assets.xml.Projectile;
import net.tiffit.realmnetapi.assets.xml.XMLLoader;
import net.tiffit.realmnetapi.map.RMap;
import net.tiffit.realmnetapi.map.object.RObject;
import net.tiffit.realmnetapi.map.projectile.ProjectileState;
import net.tiffit.realmnetapi.map.projectile.RProjectile;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.RotMGPacketIn;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class AllyShootPacketIn extends RotMGPacketIn {

    public int bulletId;
    public int ownerId;
    public short containerType;
    public byte projectileId;
    public float angle;

    @Override
    public void read(DataInputStream in) throws IOException {
        bulletId = in.readShort();
        ownerId = in.readInt();
        containerType = in.readShort();
        projectileId = in.readByte();
        angle = in.readFloat();
    }

    @Override
    public void handle(RealmNetworker net) throws IOException {
        RMap map = net.map;
        if(map.getEntityList().has(ownerId)) {
            ProjectileState state = new ProjectileState();
            state.bulletId = bulletId;
            state.ownerId = ownerId;
            state.angle = angle;
            state.team = ProjectileState.ProjectileTeam.ALLY;
            RObject shooter = map.getEntityList().get(ownerId);
            if (shooter != null) {
                state.startX = shooter.getCorrectedX();
                state.startY = shooter.getCorrectedY();
                GameObject container = XMLLoader.OBJECTS.get((int) containerType);
                if(container != null){
                    List<Projectile> projectiles = XMLLoader.OBJECTS.get((int) containerType).projectiles;
                    Projectile proj = projectiles.get(projectileId % projectiles.size());
                    if (proj != null) {
                        state.proj = proj;
                        String objId = proj.objectId;
                        state.obj = null;
                        for (GameObject obj : XMLLoader.OBJECTS.values()) {
                            if (obj.id.equals(objId)) {
                                state.obj = obj;
                                break;
                            }
                        }
                        RProjectile.create(net, state);
                    }
                }
            }
        }
    }
}
