package org.example.provisionsberechnung;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public interface BerechnungOutputPort {

    /**
     * übergibt Beträge gruppiert nach Vermittler, die berechnet wurden
     *
     * @param geldFuerVermittler das Geld welches für die jeweiligen Vermittler berechnet wurde
     */
    void sendeBerechnungsInfos(@NotNull final Map<Vermittler, BigDecimal> geldFuerVermittler);

    /**
     * markiert diese Geschaefte als berechnet für diese Provision
     *
     * @param geschaefte diese Geschaefte werden markiert
     * @param provision diese Provision wird in den Geschaeften als bereits berechnet hinterlegt
     */
    void markiereBerechnet(@NotNull Geschaeft geschaefte, @NotNull  Provision provision);
}
