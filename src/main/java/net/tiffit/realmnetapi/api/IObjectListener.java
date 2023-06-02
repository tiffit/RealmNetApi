package net.tiffit.realmnetapi.api;

import net.tiffit.realmnetapi.map.object.GameObjectState;
import net.tiffit.realmnetapi.map.object.StatType;

import java.util.Set;

public interface IObjectListener<T> {

    void updateLoop();

    void objectKill();

    class EmptyObjectListener<T> implements IObjectListener<T> {

        public EmptyObjectListener(T obj){

        }

        @Override
        public void updateLoop() {

        }

        @Override
        public void objectKill() {

        }
    }

    interface StatChangeListener {

        void onStatChange(GameObjectState state, Set<StatType> changedStats);

    }
}
