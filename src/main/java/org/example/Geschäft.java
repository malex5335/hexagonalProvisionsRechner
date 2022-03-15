package org.example;

import java.time.LocalDateTime;

public class Geschäft {

    public LocalDateTime anlieferDatum() {
        return LocalDateTime.now();
    }

    public Produkt produkt() {
        return new Produkt();
    }

    public Vermittler vermittler() {
        return new Vermittler();
    }

    public enum Status {
        SALE, LEAD
    }

    public void markiereBerechnet(boolean b) {
    }

    public Status status() {
        return Status.LEAD;
    }
}
