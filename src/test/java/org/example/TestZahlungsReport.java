package org.example;

import org.example.provisionsberechnung.Geschaeft;
import org.example.provisionsberechnung.Provision;
import org.example.provisionsberechnung.ZahlungsReport;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;

public class TestZahlungsReport implements ZahlungsReport {
    public Map<Geschaeft, Map<Provision, BigDecimal>> reportDaten = new HashMap<>();

    public static TestZahlungsReport defaultZahlungsReport() {
        return new TestZahlungsReport();
    }

    @Override
    public @NotNull List<Geschaeft> berechneteGeschaefte() {
        return new ArrayList<>(reportDaten.keySet());
    }

    @Override
    public @NotNull List<Provision> provisionenFuerGeschaeft(Geschaeft geschaeft) {
        return new ArrayList<>(reportDaten.get(geschaeft).keySet());
    }

    @Override
    public @NotNull BigDecimal betragFuerGeschaeft(Geschaeft geschaeft, Provision provision) {
        return reportDaten.get(geschaeft).get(provision);
    }

    public void ergaenze(Geschaeft geschaeft, Provision provision, BigDecimal geld) {
        var provisionsDaten = new HashMap<Provision, BigDecimal>();
        provisionsDaten.put(provision, geld);
        reportDaten.put(geschaeft, provisionsDaten);
    }
}
