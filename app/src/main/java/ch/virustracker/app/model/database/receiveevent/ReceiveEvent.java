package ch.virustracker.app.model.database.receiveevent;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import ch.virustracker.app.model.Token;
import ch.virustracker.app.model.TokenEvent;

@Entity(indices = {@Index(value = {"tokenValue", "timestampMs"}, unique = true)})
public class ReceiveEvent implements TokenEvent {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "id")
    private long id;

    // The token value that was received.
    @ColumnInfo(name = "tokenValue")
    private String tokenValue;

    // The timestamp when this token was received.
    @ColumnInfo(name = "timestampMs")
    private long timestampMs;

    // Distance to the sender of the token. Estimated through signal strength and device parameters.
    @ColumnInfo(name = "distanceMeter")
    private float distanceMeter;

    public ReceiveEvent() {
    }

    public ReceiveEvent(long timestampMs, String tokenValue, float distance) {
        this.tokenValue = tokenValue;
        this.timestampMs = timestampMs;
        this.distanceMeter = distance;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public float getDistanceMeter() {
        return distanceMeter;
    }

    public void setDistanceMeter(float distanceMeter) {
        this.distanceMeter = distanceMeter;
    }

    @Override
    public Token getToken() {
        return new Token(tokenValue);
    }

    @NonNull
    @Override
    public String toString() {
        return "ReceiveEvent { token: " + getToken() + ", timestampMs: " + getTimestampMs() + " }";
    }
}
