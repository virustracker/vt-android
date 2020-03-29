package ch.virustracker.app.model.proximityevent;

import java.util.List;

import ch.virustracker.app.model.ReportToken;
import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;

public interface IProximityEventResolver {

    List<ProximityEvent> resolveProximityEvents(List<ReceiveEvent> receiveEvents,
                                                List<ReportToken> reportTokens);
}
