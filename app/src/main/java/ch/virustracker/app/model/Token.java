package ch.virustracker.app.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return tokenValue.equals(token.tokenValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenValue);
    }

    private String tokenValue;

    @NonNull
    @Override
    public String toString() {
        return "Token { tokenValue: " + getTokenValue() + " }";
    }
}
