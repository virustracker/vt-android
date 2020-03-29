package ch.virustracker.app.model.database.advertiseevent;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import ch.virustracker.app.model.Token;
import ch.virustracker.app.model.TokenEvent;
import ch.virustracker.app.model.database.location.Location;

@Entity(indices = {@Index(value = {"tokenValue", "timestampMs"}, unique = true)})
public class AdvertiseEvent implements TokenEvent {

    public static byte[] SALT = "VIRUSTRACKER".getBytes(StandardCharsets.US_ASCII);
    public static int PREIMAGE_LENGTH = 32;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "id")
    private long id;

    // Stores the preimage value used to compute tokenValue.
    @ColumnInfo(name = "preImage")
    private byte[] preImage;

    // The token value that was advertised.
    @ColumnInfo(name = "tokenValue")
    private String tokenValue;

    // The timestamp when this token was advertised.
    @ColumnInfo(name = "timestampMs")
    private long timestampMs;

    // The location of the device at the time of the event.
    @Embedded
    public Location location;

    // Creates an empty event. Should only be used by the DAO layer.
    public AdvertiseEvent() {}

    // Generates a new token from the given preimage.
    public static AdvertiseEvent GenerateFromPreImage(byte[] preImage) {
        AdvertiseEvent event = new AdvertiseEvent();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(SALT);
            messageDigest.update(preImage);
            event.setPreImage(preImage);
            event.setTokenValue(Hex.encodeHexString(messageDigest.digest()));
            event.setTimestampMs(System.currentTimeMillis());
            // TODO set location here if we have access to the data.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return event;
    }

    // Generates a new event from a random seed using the current timestamp.
    public static AdvertiseEvent GenerateRandom() {
        Random r = new Random();
        byte[] preImage = new byte[PREIMAGE_LENGTH];
        r.nextBytes(preImage);
        assert (preImage.length == PREIMAGE_LENGTH);
        return GenerateFromPreImage(preImage);
    }

    @Override
    public Token getToken() {
        return new Token(tokenValue);
    }

    @Override
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

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public void setPreImage(byte[] preImage) {
        this.preImage = preImage;
    }

    public byte[] getPreImage() {
        return preImage;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(long timestampMs) {
        this.timestampMs = timestampMs;
    }
}
