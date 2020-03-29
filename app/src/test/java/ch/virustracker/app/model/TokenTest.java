package ch.virustracker.app.model;

import org.junit.Test;

import ch.virustracker.app.model.database.advertiseevent.AdvertiseEvent;

public class TokenTest {
    @Test
    public void testGenerate() {
        AdvertiseEvent event1 = AdvertiseEvent.GenerateRandom();
        AdvertiseEvent event2 = AdvertiseEvent.GenerateFromPreImage(event1.getPreImage());
        assert(event1.getTokenValue().equals(event2.getTokenValue()));
    }

}