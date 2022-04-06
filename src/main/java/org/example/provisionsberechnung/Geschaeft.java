package org.example.provisionsberechnung;

import java.time.LocalDateTime;

public interface Geschaeft {
    LocalDateTime anlieferDatum();

    boolean fuerVermittler(Vermittler vermittler);

    boolean fuerProdukt(Produkt produkt);

    enum Status {
        SALE, LEAD
    }

    Status status();

    boolean istBerechnetFuerKonfiguration(Konfiguration konfiguration);
}
