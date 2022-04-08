package org.example.provisionsarten;

import org.example.provisionsberechnung.Geschaeft;
import org.example.provisionsberechnung.Produkt;
import org.example.provisionsberechnung.Provision;
import org.example.provisionsberechnung.Vermittler;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

public class StandardVolumenProvision implements Provision {

    private BigDecimal prozentProGeschaeft;
    private Produkt produkt;
    private Vermittler vermittler;
    private String volumenFeld;

    public StandardVolumenProvision mitVermittler(Vermittler vermittler) {
        this.vermittler = vermittler;
        return this;
    }

    public StandardVolumenProvision mitProdukt(Produkt produkt) {
        this.produkt = produkt;
        return this;
    }

    public StandardVolumenProvision mitProzentProGeschaeft(BigDecimal prozentProGeschaeft) {
        this.prozentProGeschaeft = prozentProGeschaeft;
        return this;
    }

    public StandardVolumenProvision mitVolumenFeld(String volumenFeld) {
        this.volumenFeld = volumenFeld;
        return this;
    }

    @Override
    public BigDecimal berechneGeld(Geschaeft geschaeft) {
        return geschaeft.volumenBetraege().get(volumenFeld)
                .multiply(prozentProGeschaeft)
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
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
