package net.tiffit.realmnetapi.net.packet.out;

import net.tiffit.realmnetapi.map.object.MoveRecordState;
import net.tiffit.realmnetapi.net.packet.RotMGPacketOut;

import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MovePacketOut extends RotMGPacketOut {

    private final int tickId;
    private final int time;
    private final List<MoveRecordState> records;

    public MovePacketOut(int tickId, int time, List<MoveRecordState> records) {
        super((byte)62);
        this.tickId = tickId;
        this.time = time;
        this.records = records;
    }
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(tickId);
        out.writeInt(time);
        out.writeShort(records.size());
        for (MoveRecordState record : records) {
            out.writeInt(record.time);
            out.writeFloat(record.x);
            out.writeFloat(record.y);
        }
    }

    @Override
    public String getExtraInfo() {
        String recordsStr =
                "[" +
                records.stream().map(moveRecordState -> "" + moveRecordState.time + " (" + moveRecordState.x + ", " + moveRecordState.y + ")")
                        .collect(Collectors.joining(",")) +
                "]";
        return "{" + tickId + ", records="+ recordsStr +"}";
    }
}
