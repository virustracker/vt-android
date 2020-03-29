package ch.virustracker.app.model;

import androidx.annotation.Nullable;

// Represents the basic abstraction of a token that only has a token value (the information that is
// communicated between two clients) and can use that information to determine equality of two
// tokens.
public class Token {
    public Token(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    // Returns the token value of this token.
    public String getTokenValue() {
        return tokenValue;
    }

    @Override
    public int hashCode() {
        return getTokenValue().hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        return getTokenValue().equals(obj);
    }

    private String tokenValue;
}
