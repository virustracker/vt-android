package ch.virustracker.app.model.proximityevent;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import ch.virustracker.app.model.database.location.Location;

@Entity(indices = {@Index(value = {"timestampMs"})})
public class ProximityEvent {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "id")
    private long id;

    @ColumnInfo(name = "distance")
    private String distance;

    @ColumnInfo(name = "durationMs")
    private long durationMs;

    @ColumnInfo(name = "timestampMs")
    private long timestampMs;

    @ColumnInfo(name = "eventType")
    private String eventType;

    @ColumnInfo(name = "testResult")
    private String testResult;

    @Embedded
    private Location location;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getTestResult() {
        return testResult;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
