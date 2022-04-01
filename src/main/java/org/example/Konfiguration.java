package org.example;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

public interface Konfiguration {
    BigDecimal berechneGeld(List<Geschaeft> geschäfte, Predicate<Geschaeft> sollBerechnetWerden);

    boolean fuerProdukt(Produkt produkt);

    boolean fuerVermittler(Vermittler vermittler);
}
