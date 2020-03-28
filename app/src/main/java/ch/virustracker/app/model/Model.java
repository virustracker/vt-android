package ch.virustracker.app.model;

import java.util.List;

import ch.virustracker.app.model.database.VtDatabase;
import ch.virustracker.app.model.database.infectedtoken.InfectedToken;
import ch.virustracker.app.model.database.receivedtoken.ReceivedToken;
import ch.virustracker.app.model.proximityevent.IProximityEventProvider;
import ch.virustracker.app.model.proximityevent.ProximityEvent;

public class Model {

    private IProximityEventProvider proximityEventProvider;

    public List<ProximityEvent> getEncountersForTimeSpan(long fromTimeMs, long toTimeMs) {
        List<ReceivedToken> seenTokens = VtDatabase.getInstance().seenTokenDao().selectByTimeSpan(fromTimeMs, toTimeMs);
        List<InfectedToken> infectedTokens = VtDatabase.getInstance().infectedTokenDao().selectByTimeSpan(fromTimeMs, toTimeMs);
        return proximityEventProvider.getProximityEvents(seenTokens, infectedTokens);
    }
}
