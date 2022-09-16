package net.tiffit.realmnetapi.api;

import net.tiffit.realmnetapi.util.math.Vec2f;

public interface IPlayerPosTracker {

    Vec2f getPos();
    void setPos(Vec2f pos);

    class BasicPlayerPosTracker implements IPlayerPosTracker{
        private Vec2f pos = new Vec2f(0, 0);

        @Override
        public Vec2f getPos() {
            return pos;
        }

        @Override
        public void setPos(Vec2f pos) {
            this.pos = pos;
        }
    }
}
