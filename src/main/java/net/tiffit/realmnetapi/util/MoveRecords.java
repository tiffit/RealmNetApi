package net.tiffit.realmnetapi.util;

import net.tiffit.realmnetapi.map.object.MoveRecordState;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class MoveRecords {

    public int lastClear = -1;
    public ReentrantLock lock = new ReentrantLock();
    public List<MoveRecordState> records = new CopyOnWriteArrayList<>();

    public void addRecord(int time, float x, float y){
        int recordId = getId(time);
        if(-1 < lastClear && recordId - 1  < 10) {
            assert (records != null);
            if (records.size() == 0) {
                records.add(new MoveRecordState(time, x, y));
            } else {
                MoveRecordState latestRecord = records.get(records.size() - 1);
                if(latestRecord.x == x && latestRecord.y == y){
                    int latestRecordId = getId(latestRecord.time);
                    if(recordId == latestRecordId){
                        int currentScore = getScore(recordId, time);
                        int prevScore = getScore(recordId, latestRecord.time);
                        if(prevScore <= currentScore){
                            return;
                        }
                        latestRecord.time = time;
                        return;
                    }
                    records.add(new MoveRecordState(time, x, y));
                }else{
                    records.add(new MoveRecordState(time, x, y));
                }
            }
        }
    }

    private int getId(int time){
        return ((time - lastClear) + 50) / 100;
    }

    private int getScore(int id, int time) {
        int scoreTemp = (time - lastClear) + id * -100;
        int scoreTemp2 = scoreTemp >> 0x1f;
        return (scoreTemp ^ scoreTemp2) - scoreTemp2;
    }

    public void clear(int time){
        records.clear();
        this.lastClear = time;
    }

}