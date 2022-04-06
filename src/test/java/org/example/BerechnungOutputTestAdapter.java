package org.example;

import org.example.provisionsberechnung.BerechnungOutputPort;
import org.example.provisionsberechnung.Geschaeft;
import org.example.provisionsberechnung.Konfiguration;

import java.math.BigDecimal;
import java.util.List;

public class BerechnungOutputTestAdapter implements BerechnungOutputPort {

    public BigDecimal summe;

    @Override
    public void infoAnFreigebende(BigDecimal summe) {
        this.summe = summe;
    }

    @Override
    public void markiereBerechnet(List<Geschaeft> geschaefte, Konfiguration konfiguration) {
        geschaefte.forEach(g -> ((TestGeschaeft) g).makiereBerechnet(konfiguration));
    }
}
