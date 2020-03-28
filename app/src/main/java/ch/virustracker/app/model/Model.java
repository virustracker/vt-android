package ch.virustracker.app.model;

import java.util.List;
import java.util.Random;

import ch.virustracker.app.model.database.VtDatabase;
import ch.virustracker.app.model.database.advertisedtoken.AdvertisedToken;
import ch.virustracker.app.model.proximityevent.IProximityEventProvider;
import ch.virustracker.app.model.proximityevent.ProximityEvent;

import static ch.virustracker.app.model.Token.PREIMAGE_LENGTH;

public class Model {

    private IProximityEventProvider proximityEventProvider;
    private AdvertisedToken currentlyAdvertisedToken = null;

    public List<ProximityEvent> getEncountersForTimeSpan(long fromTimeMs, long toTimeMs) {
        return VtDatabase.getInstance().proximityEventDao().selectByTimeSpan(fromTimeMs, toTimeMs);
    }

    public AdvertisedToken getCurrentlyAdvertisedToken() {
        long timestampMs = System.currentTimeMillis();
        if (currentlyAdvertisedToken == null || currentlyAdvertisedToken.hasExpiredAt(timestampMs)) {
            Random r = new Random();
            byte[] preImage = new byte[PREIMAGE_LENGTH];
            r.nextBytes(preImage);
            currentlyAdvertisedToken = new AdvertisedToken(preImage, System.currentTimeMillis());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    VtDatabase.getInstance().advertisedTokenDao().insertAll(currentlyAdvertisedToken);
                }
            }).start();
        }
        return currentlyAdvertisedToken;
    }
}
