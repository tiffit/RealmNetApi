package net.tiffit.realmnetapi.map.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class RotMGEntityList {

    private Map<Integer, RObject> loaded_objects = new HashMap<>();
    private List<Integer> removed = new ArrayList<>();
    private Lock lock = new ReentrantLock();

    public void set(int id, RObject entity){
        lock.lock();
        loaded_objects.put(id, entity);
        lock.unlock();
    }

    public RObject get(int id){
        lock.lock();
        if(!loaded_objects.containsKey(id)){
            lock.unlock();
            return null;
        }
        RObject entity = loaded_objects.get(id);
        lock.unlock();
        return entity;
    }

    public boolean has(int id){
        lock.lock();
        boolean hasKey = loaded_objects.containsKey(id);
        lock.unlock();
        return hasKey;
    }

    public RObject get(Predicate<RObject> predicate){
        lock.lock();
        for(RObject ent : loaded_objects.values()){
            if(predicate.test(ent)){
                lock.unlock();
                return ent;
            }
        }
        lock.unlock();
        return null;
    }

    public ArrayList<RObject> getAll(Predicate<RObject> predicate){
        lock.lock();
        ArrayList<RObject> list = new ArrayList<>();
        for(RObject ent : loaded_objects.values()){
            if(predicate.test(ent)){
                list.add(ent);
            }
        }
        lock.unlock();
        return list;
    }

    public List<RObject> getEntities(){
        lock.lock();
        List<RObject> list = new ArrayList<>();
        loaded_objects.forEach((integer, rEntity) -> list.add(rEntity));
        lock.unlock();
        return list;
    }

    public RObject remove(int id){
        lock.lock();
        RObject entity = loaded_objects.remove(id);
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
