package org.example.provisionsberechnung;

import java.math.BigDecimal;
import java.util.List;

public interface BerechnungOutputPort {
    void infoAnFreigebende(final BigDecimal summe);

    void markiereBerechnet(List<Geschaeft> geschaefte, Provision provision);
}
