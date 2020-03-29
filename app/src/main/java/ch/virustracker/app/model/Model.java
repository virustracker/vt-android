package ch.virustracker.app.model;

import java.util.List;

import ch.virustracker.app.model.database.VtDatabase;
import ch.virustracker.app.model.database.advertiseevent.AdvertiseEvent;
import ch.virustracker.app.model.proximityevent.IProximityEventResolver;
import ch.virustracker.app.model.proximityevent.ProximityEvent;

public class Model {

    public static final long ROTATE_INTERVAL_MS = 5 * 60 * 1000;  // Rotate token every 5 minutes

    private IProximityEventResolver proximityEventProvider;
    private AdvertiseEvent mostRecentAdvertiseEvent = null;
    // The last time a new token was randomly generated.
    private long lastNewTokenTimestampMs;

    public List<ProximityEvent> getEncountersForTimeSpan(long fromTimeMs, long toTimeMs) {
        return VtDatabase.getInstance().proximityEventDao().selectByTimeSpan(fromTimeMs, toTimeMs);
    }

    private boolean tokenHasExpired() {
        return (System.currentTimeMillis() - lastNewTokenTimestampMs) > ROTATE_INTERVAL_MS;
    }

    // Creates a new event, creating a new token if the previous one has expired, and persists
    // the event into the database.
    public AdvertiseEvent getNewAdvertiseTokenEvent() {
        if (mostRecentAdvertiseEvent == null || tokenHasExpired()) {
            // Generate event with a new token.
            mostRecentAdvertiseEvent = AdvertiseEvent.GenerateRandom();
            lastNewTokenTimestampMs = System.currentTimeMillis();
        } else {
            // Generate event with the same token as before.
            mostRecentAdvertiseEvent =
                    AdvertiseEvent.GenerateFromPreImage(mostRecentAdvertiseEvent.getPreImage());
        }

        new Thread(() -> {
            VtDatabase.getInstance().advertiseEventDao().insertAll(mostRecentAdvertiseEvent);
        }).start();
        return mostRecentAdvertiseEvent;
    }
}
