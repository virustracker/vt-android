package ch.virustracker.app.model;

import androidx.annotation.Nullable;

public abstract class Token {

    public abstract String getTokenValue();

    @Override
    public int hashCode() {
        return getTokenValue().hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        return getTokenValue().equals(obj);
    }
}
