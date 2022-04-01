package org.example;

import java.math.BigDecimal;
import java.util.List;

public class BerechnungOutputTestAdapter implements BerechnungOutputPort {

    public BigDecimal summe;

    @Override
    public void infoAnFreigebende(BigDecimal summe) {
        this.summe = summe;
    }

    @Override
    public void markiereBerechnet(List<Geschaeft> geschäfte, Konfiguration konfiguration) {
        geschäfte.forEach(g -> ((TestGeschaeft) g).makiereBerechnet(konfiguration));
    }
}
