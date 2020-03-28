package ch.virustracker.app.model.database.receivedtoken;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import ch.virustracker.app.model.Token;

@Entity(indices = {@Index(value = {"tokenValue", "timestampMs"}, unique = true)})
public class ReceivedToken extends Token {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "id")
    private long id;

    @ColumnInfo(name = "tokenValue")
    private String tokenValue;

    @ColumnInfo(name = "timestampMs")
    private float timestampMs;

    @ColumnInfo(name = "signalStrength")
    private long signalStrength;

    public ReceivedToken() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
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

    @Override
    public String getTokenValue() {
        return tokenValue;
    }

}
