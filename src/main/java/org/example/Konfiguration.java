package org.example;

import java.math.BigDecimal;

public interface Konfiguration {
    default BigDecimal berechneGeld(Geschäft geschäft) {
        return BigDecimal.ZERO;
    }
}
