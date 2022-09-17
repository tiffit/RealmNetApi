package net.tiffit.realmnetapi.net.ack;

import net.tiffit.realmnetapi.net.RealmNetworker;
import net.tiffit.realmnetapi.net.packet.out.ShootAckPacketOut;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class AckHandler {
    private final RealmNetworker net;

    private final Queue<Runnable> acks = new ConcurrentLinkedQueue<>();
    private final ReentrantLock lock = new ReentrantLock();

    private final AtomicInteger shootAckCounter = new AtomicInteger(0);

    public AckHandler(RealmNetworker net) {
        this.net = net;
    }

    // Before Run
    public void add(Runnable runnable){
        lock.lock();
        addShootAcks();
        acks.add(runnable);
        lock.unlock();
    }

    // During run
    public void addInvalidShoot(){
        resetShootAcks();
        net.send(new ShootAckPacketOut(-1, (short)1));
    }

    // During run
    public void addShoot(){
        lock.lock();
        shootAckCounter.incrementAndGet();
        lock.unlock();
    }

    // Before run
    private void addShootAcks(){
        acks.add(this::resetShootAcks);
    }

    // During run
    private void resetShootAcks(){
        if(shootAckCounter.get() > 0){
            short count = shootAckCounter.shortValue();
            net.send(new ShootAckPacketOut(RealmNetworker.getTime(), count));
            shootAckCounter.set(0);
        }
    }

    public void process(){
        lock.lock();
        addShootAcks();
        while(!acks.isEmpty()){
            acks.poll().run();
        }
        resetShootAcks();
        lock.unlock();
    }

}
