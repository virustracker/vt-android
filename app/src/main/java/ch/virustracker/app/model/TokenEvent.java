package ch.virustracker.app.model;

// Represents a token event, corresponding to the receiption or transmission or a token.
public interface TokenEvent {
    // Returns the token underlying the event.
    public Token getToken();

    // Returns the timestamp of the token event.
    public long getTimestampMs();

}
