package net.tiffit.realmnetapi.map.object;

public class MoveRecordState {

    public int time;
    public float x;
    public float y;

    public MoveRecordState(int time, float x, float y){
        this.time = time;
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return String.format("time=%s, x=%.5f, y=%.5f", time, x, y);
    }

}
