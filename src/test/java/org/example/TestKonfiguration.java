package org.example;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TestKonfiguration implements Konfiguration {
    private Produkt produkt;
    private Vermittler vermittler;
    private final Map<Geschaeft, BigDecimal> geldProGeschaeft = new HashMap<>();

    @Override
    public BigDecimal berechneGeld(List<Geschaeft> geschaefte, Predicate<Geschaeft> sollBerechnetWerden) {
        var summe = BigDecimal.ZERO;
        for(var geschaeft : geschaefte) {
            if(sollBerechnetWerden.test(geschaeft)
                    && geldProGeschaeft.containsKey(geschaeft)) {
                summe = summe.add(geldProGeschaeft.get(geschaeft));
            }
        }
        return summe;
    }

    @Override
    public boolean fuerProdukt(Produkt produkt) {
        return this.produkt.equals(produkt);
    }

    @Override
    public boolean fuerVermittler(Vermittler vermittler) {
        return this.vermittler.equals(vermittler);
    }

    public TestKonfiguration mitGeldFuerGeschaeft(Geschaeft geschaeft, BigDecimal geld) {
        geldProGeschaeft.put(geschaeft, geld);
        return this;
    }

    public TestKonfiguration mitProdukt(Produkt produkt) {
        this.produkt = produkt;
        return this;
    }

    public TestKonfiguration mitVermittler(Vermittler vermittler) {
        this.vermittler = vermittler;
        return this;
    }
}
