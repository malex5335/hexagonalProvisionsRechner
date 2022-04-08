package org.example.provisionsberechnung;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public interface Geschaeft {
    LocalDateTime anlieferDatum();

    boolean fuerVermittler(Vermittler vermittler);

    boolean fuerProdukt(Produkt produkt);

    enum Status {
        SALE, LEAD
    }

    Status status();

    Map<String, BigDecimal> volumenBetraege();

    boolean istBerechnetFuerProvision(Provision provision);
}
