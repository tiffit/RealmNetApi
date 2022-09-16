package net.tiffit.realmnetapi.util;

import net.tiffit.realmnetapi.map.projectile.ProjectileState;
import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.out.ShootAckPacketOut;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ShootAckCounter {

    private static AtomicInteger INVALID = new AtomicInteger(-1);

    private static Deque<AtomicInteger> ackCounter = new ConcurrentLinkedDeque<>();
    private static ConcurrentLinkedQueue<ProjectileState[]> pending = new ConcurrentLinkedQueue<>();
    private static AtomicInteger pendingLimit = new AtomicInteger(0);

    public static void add(){
        if(ackCounter.size() == 0 || ackCounter.getLast().get() == -1){
            ackCounter.add(new AtomicInteger(1));
        }else{
            ackCounter.getLast().incrementAndGet();
        }
    }

    public static void addInvalid(){
        ackCounter.add(INVALID);
    }

    public static List<Integer> getValues(){
        AtomicInteger val;
        List<Integer> list = new LinkedList<>();
        while((val = ackCounter.poll()) != null){
            list.add(val.get());
        }
        return list;
    }

    public static void addPending(ProjectileState[] states){
        pending.add(states);
    }

    public static void ack(RealmNetworker net, int time){
        for (Integer shootAck : getValues()) {
            if(shootAck == -1){
                net.send(new ShootAckPacketOut(-1, (short)1));
            }else{
                net.send(new ShootAckPacketOut(time, shootAck.shortValue()));
                pendingLimit.addAndGet(shootAck);
            }
        }
    }

    public static ProjectileState[] pollPending(){
        if(pendingLimit.get() == 0){
            return null;
        }
        pendingLimit.getAndDecrement();
        return pending.poll();
    }

}
