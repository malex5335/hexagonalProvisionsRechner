package org.example;

import java.time.LocalDateTime;

public interface Geschäft {

    LocalDateTime anlieferDatum();

    Produkt produkt();

    Vermittler vermittler();

    enum Status {
        SALE, LEAD
    }

    Status status();
}
