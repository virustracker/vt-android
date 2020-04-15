package ch.virustracker.app.model.database.receiveevent;

import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.dpppt.android.sdk.internal.database.models.Handshake;

import java.util.ArrayList;
import java.util.List;

import ch.virustracker.app.controller.DistanceUtil;
import ch.virustracker.app.model.ReportToken;
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

    public ReceiveEvent(Handshake handshake) {
        this.tokenValue = Base64.encodeToString(handshake.getEphId(), Base64.NO_WRAP);
        this.timestampMs = handshake.getTimestamp();
        this.distanceMeter = DistanceUtil.getDistanceFromRssi(handshake.getRssi());
    }

    public static List<ReceiveEvent> getReceiveEventsFromHandshakes(List<Handshake> handshakes) {
        List<ReceiveEvent> list = new ArrayList(handshakes.size());
        for (Handshake handshake : handshakes) {
            ReceiveEvent receiveEvent = new ReceiveEvent(handshake);
            list.add(receiveEvent);
        }
        return list;
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
