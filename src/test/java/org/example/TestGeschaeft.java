package org.example;

import org.example.provisionsberechnung.Geschaeft;
import org.example.provisionsberechnung.Provision;
import org.example.provisionsberechnung.Produkt;
import org.example.provisionsberechnung.Vermittler;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class TestGeschaeft implements Geschaeft {
    private LocalDateTime anlieferDatum = LocalDateTime.now();
    private Produkt produkt;
    public List<Provision> fuerKonfigsBerechnet = new ArrayList<>();
    private Vermittler vermittler;
    private Status status = Status.LEAD;
    private final Map<String, BigDecimal> volumenBetraege = new HashMap<>();

    public static TestGeschaeft defaultGeschaeft() {
        return new TestGeschaeft()
                .mitAnlieferDatum(LocalDateTime.now().minusDays(120))
                .mitStatus(Geschaeft.Status.SALE);
    }


    @Override
    public @NotNull LocalDateTime anlieferDatum() {
        return this.anlieferDatum;
    }

    @Override
    public boolean fuerVermittler(@NotNull Vermittler vermittler) {
        if(this.vermittler != null) {
            return this.vermittler.vermittlerNummer().equals(vermittler.vermittlerNummer());
        }
        return false;
    }

    @Override
    public boolean fuerProdukt(@NotNull Produkt produkt) {
        return this.produkt.equals(produkt);
    }

    @Override
    public @NotNull Status status() {
        return this.status;
    }

    @Override
    public @NotNull Map<String, BigDecimal> volumenBetraege() {
        return volumenBetraege;
    }

    @Override
    public boolean istBerechnetFuerProvision(Provision provision) {
        return fuerKonfigsBerechnet.stream()
                .anyMatch(k -> k.equals(provision));
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

    public TestGeschaeft mitVolumenBetrag(String feld, BigDecimal betrag) {
        volumenBetraege.put(feld, betrag);
        return this;
    }

    public TestGeschaeft mitBerechnetFuer(TestProvision konfiguration) {
        fuerKonfigsBerechnet.add(konfiguration);
        return this;
    }

    public void makiereBerechnet(Provision provision) {
        fuerKonfigsBerechnet.add(provision);
    }

}
