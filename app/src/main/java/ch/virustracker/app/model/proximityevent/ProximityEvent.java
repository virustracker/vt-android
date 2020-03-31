package ch.virustracker.app.model.proximityevent;

import androidx.annotation.NonNull;
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

    @ColumnInfo(name = "durationMs")
    private long durationMs;

    @ColumnInfo(name = "timestampMs")
    private long timestampMs;

    @ColumnInfo(name = "reportType")
    private String reportType;

    @ColumnInfo(name = "testResult")
    private String testResult;

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

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getTestResult() {
        return testResult;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "ProximityEvent { distance: " + getDistance() + ", durationMs: " + getDurationMs() +
                ", timestampMs: " + getTimestampMs() + ", testResult: " + getTestResult() + ", " +
                "reportType: " + getReportType() + " }";
    }
}
