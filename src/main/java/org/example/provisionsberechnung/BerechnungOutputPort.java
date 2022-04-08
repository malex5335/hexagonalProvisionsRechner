package org.example.provisionsberechnung;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

public interface BerechnungOutputPort {

    void infoAnFreigebende(@NotNull final BigDecimal summe);

    /**
     * markiert diese Geschaefte als berechnet für diese Provision
     *
     * @param geschaefte diese Geschaefte werden markiert
     * @param provision diese Provision wird in den Geschaeften als bereits berechnet hinterlegt
     */
    void markiereBerechnet(@NotNull List<Geschaeft> geschaefte, @NotNull  Provision provision);
}
