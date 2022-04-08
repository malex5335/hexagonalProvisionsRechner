package org.example;

import org.example.provisionsberechnung.BerechnungOutputPort;
import org.example.provisionsberechnung.Geschaeft;
import org.example.provisionsberechnung.Provision;
import org.example.provisionsberechnung.Vermittler;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BerechnungOutputTestAdapter implements BerechnungOutputPort {

    public Map<Vermittler, BigDecimal> geldFuerVermittler = new HashMap<>();

    @Override
    public void sendeBerechnungsInfos(@NotNull Map<Vermittler, BigDecimal> geldFuerVermittler) {
        for(var vermittler : geldFuerVermittler.keySet()) {
            if(!this.geldFuerVermittler.containsKey(vermittler)) {
                this.geldFuerVermittler.put(vermittler, geldFuerVermittler.get(vermittler));
            } else {
                this.geldFuerVermittler.put(vermittler,
                        this.geldFuerVermittler.get(vermittler).add(geldFuerVermittler.get(vermittler)));
            }
        }
    }

    @Override
    public void markiereBerechnet(@NotNull Geschaeft geschaeft, @NotNull Provision provision) {
        ((TestGeschaeft) geschaeft).makiereBerechnet(provision);
    }

    public BigDecimal geldFuerVermittler(TestVermittler vermittler) {
        return geldFuerVermittler.getOrDefault(vermittler, BigDecimal.ZERO);
    }
}
