package ch.virustracker.app.model.proximityevent;

import java.util.List;

import ch.virustracker.app.model.ReportToken;
import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;

public interface IProximityEventProvider {

    List<ProximityEvent> getProximityEvents(List<ReceiveEvent> receiveEvents,
                                            List<ReportToken> reportTokens);
}
