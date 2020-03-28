package ch.virustracker.app.model.database.seentoken;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"token", "timestampMs"}, unique = true)})
public class SeenToken {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "id")
    private long id;

    @ColumnInfo(name = "token")
    private String token;

    @ColumnInfo(name = "timestampMs")
    private float timestampMs;

    @ColumnInfo(name = "signalStrength")
    private long signalStrength;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public float getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(float timestampMs) {
        this.timestampMs = timestampMs;
    }

    public long getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(long signalStrength) {
        this.signalStrength = signalStrength;
    }
}
