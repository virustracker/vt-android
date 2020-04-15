package ch.virustracker.app.model.proximityevent;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import ch.virustracker.app.model.ReportToken;
import ch.virustracker.app.model.database.proximityevent.Distance;

@Entity(indices = {@Index(value = {"timestampMs"})})
public class ProximityEvent {

    public static final int RISK_HIGH = 2;
    public static final int RISK_LOW = 1;
    public static final int RISK_NO = 0;

    public static final long HIGH_RISK_MIN_DURATION_MS = 1000*60*15;
    public static final long LOW_RISK_MIN_DURATION_MS = 1000*60*15;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "id")
    private long id;

    @ColumnInfo(name = "distance")
    private String distance = Distance.LOW_DIST;

    @ColumnInfo(name = "durationMs")
    private long durationMs = 0;

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

    public int getRisk() {
        if (testResult != null && testResult.equals(ReportToken.NEGATIVE)) return RISK_NO;
        if (durationMs > HIGH_RISK_MIN_DURATION_MS && distance.equals(Distance.LOW_DIST)) return RISK_HIGH;
        if (durationMs > HIGH_RISK_MIN_DURATION_MS && distance.equals(Distance.HIGH_DIST)) return RISK_LOW;
        if (durationMs < HIGH_RISK_MIN_DURATION_MS && distance.equals(Distance.LOW_DIST)) return RISK_LOW;
        return RISK_NO;
    }

    @NonNull
    @Override
    public String toString() {
        return "ProximityEvent { distance: " + getDistance() + ", durationMs: " + getDurationMs() +
                ", timestampMs: " + getTimestampMs() + ", testResult: " + getTestResult() + ", " +
                "reportType: " + getReportType() + " }";
    }
}
