package org.example;

public interface Produkt {

    default boolean aktiv(Vermittler vermittler) {
        return false;
    }
}
