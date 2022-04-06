package org.example.provisionsarten;

import org.example.provisionsberechnung.Geschaeft;
import org.example.provisionsberechnung.Konfiguration;
import org.example.provisionsberechnung.Produkt;
import org.example.provisionsberechnung.Vermittler;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

public class StandardProvision implements Konfiguration {

    private final BigDecimal geldProGeschaeft;
    private final Produkt produkt;
    private final Vermittler vermittler;

    public StandardProvision(Produkt produkt, Vermittler vermittler, BigDecimal geldProGeschaeft) {
        this.produkt = produkt;
        this.vermittler = vermittler;
        this.geldProGeschaeft = geldProGeschaeft;
    }

    @Override
    public BigDecimal berechneGeld(Geschaeft geschaeft) {
        return geldProGeschaeft;
    }

    @Override
    public boolean fuerProdukt(Produkt produkt) {
        if(this.produkt != null && produkt != null) {
            return this.produkt.produktName().equals(produkt.produktName());
        }
        return produkt == null;
    }

    @Override
    public boolean fuerVermittler(Vermittler vermittler) {
        if(this.vermittler != null && vermittler != null) {
            return this.vermittler.vermittlerNummer().equals(vermittler.vermittlerNummer());
        }
        return vermittler == null;
    }
}
