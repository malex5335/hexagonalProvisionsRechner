package org.example.provisionsarten;

import org.example.provisionsberechnung.Geschaeft;
import org.example.provisionsberechnung.Konfiguration;
import org.example.provisionsberechnung.Produkt;
import org.example.provisionsberechnung.Vermittler;

import java.math.BigDecimal;

public class StandardProvision implements Konfiguration {

    private BigDecimal geldProGeschaeft;
    private Produkt produkt;
    private Vermittler vermittler;

    public StandardProvision(Produkt produkt, Vermittler vermittler, BigDecimal geldProGeschaeft) {
        mitProdukt(produkt);
        mitVermitter(vermittler);
        mitGeldProGeschaeft(geldProGeschaeft);
    }

    public void mitVermitter(Vermittler vermittler) {
        this.vermittler = vermittler;
    }

    public void mitProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public void mitGeldProGeschaeft(BigDecimal geldProGeschaeft) {
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
