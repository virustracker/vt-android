package ch.virustracker.app.model.proximityevent;

import java.util.List;

import ch.virustracker.app.model.database.infectedtoken.InfectedToken;
import ch.virustracker.app.model.database.receivedtoken.ReceivedToken;

public interface IProximityEventProvider {

    List<ProximityEvent> getProximityEvents(List<ReceivedToken> seenTokens, List<InfectedToken> infectedTokens);
}
