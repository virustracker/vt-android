package ch.virustracker.app.model.database.infectedtoken;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import ch.virustracker.app.model.Token;

@Entity(indices = {@Index(value = {"tokenValue", "timestampMs"}, unique = true)})
public class InfectedToken extends Token {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "id")
    private long id;

    @ColumnInfo(name = "tokenValue")
    private String tokenValue;

    @ColumnInfo(name = "timestampMs")
    private float timestampMs;

    public InfectedToken() {}

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

    @Override
    public String getTokenValue() {
        return tokenValue;
    }
}
