package ch.virustracker.app.model.proximityevent;

import java.util.List;
import java.util.Set;

import ch.virustracker.app.model.ReportToken;
import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;

public interface IProximityEventResolver {

    // Given the list of ReceiveEvents, received by this device from other clients, and the list
    // of ReportTokens, obtained from the server, this method determines the list of
    // ProximityEvents, where the user was likely close to a likely infected person.
    List<ProximityEvent> resolveProximityEvents(List<ReceiveEvent> receiveEvents,
                                                List<ReportToken> reportTokens);
}
