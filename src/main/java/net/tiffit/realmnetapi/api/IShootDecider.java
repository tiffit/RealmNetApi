package net.tiffit.realmnetapi.api;

import net.tiffit.realmnetapi.assets.xml.GameObject;

public interface IShootDecider {

    boolean shouldShoot();
    float getAngleRads(GameObject go, float arcGap);

    class ShootDecider implements IShootDecider {

        @Override
        public boolean shouldShoot() {
            return false;
        }

        @Override
        public float getAngleRads(GameObject go, float arcGap){
            return 0f;
        }
    }
}
