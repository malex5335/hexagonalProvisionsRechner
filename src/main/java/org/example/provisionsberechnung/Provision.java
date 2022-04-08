package org.example.provisionsberechnung;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

public interface Provision {
    BigDecimal berechneGeld(Geschaeft geschaeft);

    boolean fuerProdukt(Produkt produkt);

    boolean fuerVermittler(Vermittler vermittler);
}
