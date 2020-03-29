package ch.virustracker.app.model.proximityevent;

import java.util.List;

import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;
import ch.virustracker.app.model.database.servertoken.ReportToken;

public interface IProximityEventProvider {

    List<ProximityEvent> getProximityEvents(List<ReceiveEvent> receiveEvents, List<ReportToken> reportTokens);
}
