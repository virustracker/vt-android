package ch.virustracker.app.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.Nullable;

public class Token {

    public static byte[] SALT = "virustracker".getBytes(StandardCharsets.US_ASCII);
    public static int PREIMAGE_LENGTH = 32;
    public static final long ROTATE_INTERVAL_MS = 5*60*1000;  // Rotate token every 5 minutes

    private long slot;

    public Token() {}

    public Token(byte[] preImage, long timestampMs) {
        assert(preImage.length == PREIMAGE_LENGTH);
        slot = timestampMs/ROTATE_INTERVAL_MS;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
            messageDigest.update(SALT);
            messageDigest.update(preImage);
            setTokenValue(new String(messageDigest.digest(), StandardCharsets.US_ASCII));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public long getSlot() {
        return slot;
    }

    public void setSlot(long slot) {
        this.slot = slot;
    }

    public String getTokenValue() {return "";}

    public void setTokenValue(String tokenValue) {}

    @Override
    public int hashCode() {
        return getTokenValue().hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        return getTokenValue().equals(obj);
    }
}
