package ch.virustracker.app.model.database.receiveevent;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import ch.virustracker.app.model.Token;
import ch.virustracker.app.model.TokenEvent;
import ch.virustracker.app.model.database.location.Location;

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

    // The strength of the signal through which the token was received. This can be used later on
    // to determine the proximity of sender and receiver.
    @ColumnInfo(name = "signalStrength")
    private long signalStrength;

    // The location of the device at the time of the event.
    @Embedded
    public Location location;

    public ReceiveEvent() {
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

    @Override
    public Location getLocation() {
        return null;
    }

    public void setLocation(Location location) { this.location = location; }

    public long getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(long signalStrength) {
        this.signalStrength = signalStrength;
    }

    @Override
    public Token getToken() {
        return new Token(tokenValue);
    }

}