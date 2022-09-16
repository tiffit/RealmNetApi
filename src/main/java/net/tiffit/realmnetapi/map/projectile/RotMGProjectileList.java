package net.tiffit.realmnetapi.map.projectile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class RotMGProjectileList {

    private LinkedHashMap<Integer, RProjectile> loaded_projectiles = new LinkedHashMap<>();
    private List<Integer> removed = new ArrayList<>();
    private Lock lock = new ReentrantLock();

    public void add(RProjectile entity){
        lock.lock();
        loaded_projectiles.put(entity.getProjectileId(), entity);
        lock.unlock();
    }

    public RProjectile get(int id){
        lock.lock();
        if(!loaded_projectiles.containsKey(id)){
            lock.unlock();
            return null;
        }
        RProjectile entity = loaded_projectiles.get(id);
        lock.unlock();
        return entity;
    }

    public boolean has(int id){
        lock.lock();
        boolean hasKey = loaded_projectiles.containsKey(id);
        lock.unlock();
        return hasKey;
    }

    public RProjectile get(Predicate<RProjectile> predicate){
        lock.lock();
        for(RProjectile ent : loaded_projectiles.values()){
            if(predicate.test(ent)){
                lock.unlock();
                return ent;
            }
        }
        lock.unlock();
        return null;
    }

    public ArrayList<RProjectile> getAll(Predicate<RProjectile> predicate){
        lock.lock();
        ArrayList<RProjectile> list = new ArrayList<>();
        for(RProjectile ent : loaded_projectiles.values()){
            if(predicate.test(ent)){
                list.add(ent);
            }
        }
        lock.unlock();
        return list;
    }

    public List<RProjectile> getProjectiles(){
        lock.lock();
        List<RProjectile> list = new ArrayList<>();
        loaded_projectiles.forEach((integer, RProjectile) -> list.add(RProjectile));
        lock.unlock();
        return list;
    }

    public RProjectile remove(int id){
        lock.lock();
        RProjectile entity = loaded_projectiles.remove(id);
        removed.add(id);
        lock.unlock();
        return entity;
    }

    public boolean isRemoved(int id){
        lock.lock();
        boolean isRemoved = removed.contains(id);
        lock.unlock();
        return isRemoved;
    }
}
