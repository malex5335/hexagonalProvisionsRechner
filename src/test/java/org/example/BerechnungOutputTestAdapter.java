package org.example;

import org.example.provisionsberechnung.BerechnungOutputPort;
import org.example.provisionsberechnung.Geschaeft;
import org.example.provisionsberechnung.Provision;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class BerechnungOutputTestAdapter implements BerechnungOutputPort {

    public BigDecimal summe = BigDecimal.ZERO;

    @Override
    public void infoAnFreigebende(@NotNull BigDecimal summe) {
        this.summe = this.summe.add(summe);
    }

    @Override
    public void markiereBerechnet(@NotNull List<Geschaeft> geschaefte, @NotNull Provision provision) {
        geschaefte.forEach(g -> ((TestGeschaeft) g).makiereBerechnet(provision));
    }
}
