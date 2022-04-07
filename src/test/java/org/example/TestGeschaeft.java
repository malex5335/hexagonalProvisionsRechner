package org.example;

import org.example.provisionsberechnung.Geschaeft;
import org.example.provisionsberechnung.Konfiguration;
import org.example.provisionsberechnung.Produkt;
import org.example.provisionsberechnung.Vermittler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestGeschaeft implements Geschaeft {
    private LocalDateTime anlieferDatum;
    private Produkt produkt;
    public List<Konfiguration> fuerKonfigsBerechnet = new ArrayList<>();
    private Vermittler vermittler;
    private Status status;

    public static TestGeschaeft defaultGeschaeft() {
        return new TestGeschaeft()
                .mitAnlieferDatum(LocalDateTime.now().minusDays(120))
                .mitStatus(Geschaeft.Status.SALE);
    }

    @Override
    public LocalDateTime anlieferDatum() {
        return anlieferDatum;
    }

    @Override
    public boolean fuerVermittler(Vermittler vermittler) {
        return this.vermittler.vermittlerNummer().equals(vermittler.vermittlerNummer());
    }

    @Override
    public boolean fuerProdukt(Produkt produkt) {
        return this.produkt.equals(produkt);
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public boolean istBerechnetFuerKonfiguration(Konfiguration konfiguration) {
        return fuerKonfigsBerechnet.stream()
                .anyMatch(k -> k.equals(konfiguration));
    }

    public TestGeschaeft mitAnlieferDatum(LocalDateTime anlieferDatum) {
        this.anlieferDatum = anlieferDatum;
        return this;
    }

    public TestGeschaeft mitVermittler(Vermittler vermittler) {
        this.vermittler = vermittler;
        return this;
    }

    public TestGeschaeft mitProdukt(Produkt produkt) {
        this.produkt = produkt;
        return this;
    }

    public TestGeschaeft mitStatus(Status status) {
        this.status = status;
        return this;
    }

    public TestGeschaeft mitBerechnetFuer(TestKonfiguration konfiguration) {
        fuerKonfigsBerechnet.add(konfiguration);
        return this;
    }

    public void makiereBerechnet(Konfiguration konfiguration) {
        fuerKonfigsBerechnet.add(konfiguration);
    }
}
