package ch.virustracker.app.model.proximityevent;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"timestampMs"})})
public class ProximityEvent {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "id")
    private long id;

    @ColumnInfo(name = "distance")
    private String distance;

    @ColumnInfo(name = "timestampMs")
    private long timestampMs;

    @ColumnInfo(name = "durationMs")
    private long durationMs;

    @ColumnInfo(name = "infectionState")
    private String infectionState;

    @ColumnInfo(name = "confidentialEncounter")
    private boolean confidentialEncounter;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public String getInfectionState() {
        return infectionState;
    }

    public void setInfectionState(String infectionState) {
        this.infectionState = infectionState;
    }

    public boolean isConfidentialEncounter() {
        return confidentialEncounter;
    }

    public void setConfidentialEncounter(boolean confidentialEncounter) {
        this.confidentialEncounter = confidentialEncounter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
