package org.example;

import org.example.provisionsberechnung.BerechnungOutputPort;
import org.example.provisionsberechnung.Geschaeft;
import org.example.provisionsberechnung.Provision;

import java.math.BigDecimal;
import java.util.List;

public class BerechnungOutputTestAdapter implements BerechnungOutputPort {

    public BigDecimal summe = BigDecimal.ZERO;

    @Override
    public void infoAnFreigebende(BigDecimal summe) {
        this.summe = this.summe.add(summe);
    }

    @Override
    public void markiereBerechnet(List<Geschaeft> geschaefte, Provision provision) {
        geschaefte.forEach(g -> ((TestGeschaeft) g).makiereBerechnet(provision));
    }
}
