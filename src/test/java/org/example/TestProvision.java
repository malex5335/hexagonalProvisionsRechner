package org.example;

import org.example.provisionsberechnung.Geschaeft;
import org.example.provisionsberechnung.Provision;
import org.example.provisionsberechnung.Produkt;
import org.example.provisionsberechnung.Vermittler;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class TestProvision implements Provision {

    private BigDecimal geldProGeschaeft;
    private Produkt produkt;
    private Vermittler vermittler;

    public static TestProvision defaultProvision() {
        return new TestProvision();
    }

    public TestProvision mitProdukt(Produkt produkt){
        this.produkt = produkt;
        return this;
    }

    public TestProvision mitVermittler(Vermittler vermittler){
        this.vermittler = vermittler;
        return this;
    }

    public TestProvision mitGeldProGeschaeft(BigDecimal geldProGeschaeft){
        this.geldProGeschaeft = geldProGeschaeft;
        return this;
    }

    @Override
    public @NotNull BigDecimal berechneGeld(@NotNull Geschaeft geschaeft) {
        return geldProGeschaeft;
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
