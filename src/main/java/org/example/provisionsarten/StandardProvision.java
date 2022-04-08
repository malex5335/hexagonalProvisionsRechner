package org.example.provisionsarten;

import org.example.provisionsberechnung.Geschaeft;
import org.example.provisionsberechnung.Provision;
import org.example.provisionsberechnung.Produkt;
import org.example.provisionsberechnung.Vermittler;

import java.math.BigDecimal;
import java.util.Objects;

public class StandardProvision implements Provision {

    private BigDecimal geldProGeschaeft;
    private Produkt produkt;
    private Vermittler vermittler;

    public StandardProvision mitVermittler(Vermittler vermittler) {
        this.vermittler = vermittler;
        return this;
    }

    public StandardProvision mitProdukt(Produkt produkt) {
        this.produkt = produkt;
        return this;
    }

    public StandardProvision mitGeldProGeschaeft(BigDecimal geldProGeschaeft) {
        this.geldProGeschaeft = geldProGeschaeft;
        return this;
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
        return Objects.equals(vermittler, this.vermittler);
    }
}
