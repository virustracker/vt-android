package ch.virustracker.app.model.proximityevent;

import java.util.List;

import ch.virustracker.app.model.database.infectedtoken.InfectedToken;
import ch.virustracker.app.model.database.seentoken.SeenToken;

public interface IProximityEventProvider {

    List<ProximityEvent> getProximityEvents(List<SeenToken> seenTokens, List<InfectedToken> infectedTokens);
}
