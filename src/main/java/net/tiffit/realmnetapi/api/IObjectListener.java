package net.tiffit.realmnetapi.api;

public interface IObjectListener<T> {

    void updateLoop();

    void objectKill();

    class EmptyObjectListener<T> implements IObjectListener {

        public EmptyObjectListener(T obj){

        }

        @Override
        public void updateLoop() {

        }

        @Override
        public void objectKill() {

        }
    }
}
