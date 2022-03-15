package org.example;

import java.time.LocalDateTime;

public interface Geschäft {

    default LocalDateTime anlieferDatum() {
        return LocalDateTime.now();
    }

    default Produkt produkt() {
        return null;
    }

    default Vermittler vermittler() {
        return null;
    }

    enum Status {
        SALE, LEAD
    }

    default Status status() {
        return Status.LEAD;
    }
}
