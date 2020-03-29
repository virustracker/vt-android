package ch.virustracker.app.model;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class TokenTest {

    @Test
    public void testGenerate() {
        Token t = new Token("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes(StandardCharsets.US_ASCII), 0) {

            private String tokenValue;

            @Override
            public String getTokenValue() {
                return tokenValue;
            }

            @Override
            public void setTokenValue(String tokenValue) {
                this.tokenValue = tokenValue;
            }
        };
        assert(t.getTokenValue().equals("082c1193104576b8b8bf1594b2b32aeb520abb72fc9274826e169fd85c358a53"));
        assert(t.getSlot() == 0);

        //Tokens are not time dependent
        Token t2 = new Token("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes(StandardCharsets.US_ASCII), 300*1000) {

            private String tokenValue;

            @Override
            public String getTokenValue() {
                return tokenValue;
            }

            @Override
            public void setTokenValue(String tokenValue) {
                this.tokenValue = tokenValue;
            }
        };
        assert(t.getTokenValue().equals(t2.getTokenValue()) && t.getSlot() != t2.getSlot());
    }

}