package ch.virustracker.app.model;

import ch.virustracker.app.model.database.location.Location;

// Represents a token event, corresponding to the receiption or transmission or a token.
public interface TokenEvent {
    // Returns the token underlying the event.
    public Token getToken();

    // Returns the timestamp of the token event.
    public long getTimestampMs();

    // Returns the location of the device (if available) at the time of the token event.
    public Location getLocation();
}
