package org.example;

import org.example.provisionsberechnung.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

import static org.example.provisionsberechnung.TestZahlungsReport.defaultZahlungsReport;

public class TestVermittler implements Vermittler {

    private String vermittlerNummer;
    private Vermittler haupVermittler;
    private TestZahlungsReport zahlungsReport;

    public static TestVermittler defaultVermittler() {
        return new TestVermittler()
                .mitVermittlerNummer(UUID.randomUUID().toString())
                .mitZahlungsReport(defaultZahlungsReport());
    }

    @Override
    public @NotNull String vermittlerNummer() {
        return this.vermittlerNummer;
    }

    @Override
    public Vermittler hauptVermittler() {
        return this.haupVermittler;
    }

    @Override
    public @NotNull ZahlungsReport zahlungsReport() {
        return this.zahlungsReport;
    }

    @Override
    public boolean ergaenzeInZahlungsReport(@NotNull Geschaeft geschaeft, @NotNull Provision provision, @NotNull BigDecimal geld) {
        this.zahlungsReport.ergaenze(geschaeft, provision, geld);
        return true;
    }

    public  TestVermittler mitVermittlerNummer(String vermittlerNummer) {
        this.vermittlerNummer = vermittlerNummer;
        return this;
    }

    public TestVermittler mitHauptVermittler(Vermittler vermittler) {
        this.haupVermittler = vermittler;
        return this;
    }

    public TestVermittler mitZahlungsReport(TestZahlungsReport zahlungsReport) {
        this.zahlungsReport = zahlungsReport;
        return this;
    }
}
