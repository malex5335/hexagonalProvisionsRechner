package org.example.provisionsarten;

import org.example.provisionsberechnung.Geschaeft;
import org.example.provisionsberechnung.Produkt;
import org.example.provisionsberechnung.Provision;
import org.example.provisionsberechnung.Vermittler;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull BigDecimal berechneGeld(@NotNull Geschaeft geschaeft) {
        return geschaeft.volumenBetraege().get(volumenFeld)
                .multiply(prozentProGeschaeft)
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean fuerProdukt(@NotNull Produkt produkt) {
        if(this.produkt != null) {
            return this.produkt.produktName().equals(produkt.produktName());
        }
        return false;
    }

    @Override
    public boolean fuerVermittler(@NotNull Vermittler vermittler) {
        if(this.vermittler != null) {
            return this.vermittler.vermittlerNummer().equals(vermittler.vermittlerNummer());
        }
        return false;
    }
}
