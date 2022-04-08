package org.example.provisionsberechnung;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public interface Vermittler {

    /**
     * @return die eindeutige Nummer des Vermittlers
     */
    @NotNull
    String vermittlerNummer();

    /**
     * gibt den Hauptvermittler an, dem dieser Vermittler untergeordnet ist<br>
     * kann null sein
     *
     * @return der übergeordnete Vermittler oder null, wenn es diesen nicht gibt
     */
    @Nullable
    Vermittler hauptVermittler();

    @NotNull
    ZahlungsReport zahlungsReport();

    /**
     * fügt einen Eintrag in den Zahlungsreport hinzu
     *
     * @param geschaeft dieses Geschaeft wurde berechnet
     * @param provision mit dieser Provision wurde das Geschaeft berechnet
     * @param geld diese Menge an Geld wurde berechnet
     * @return true, wenn es keine Probleme beim Eintragen gab
     */
    boolean ergaenzeInZahlungsReport(@NotNull Geschaeft geschaeft, @NotNull Provision provision, @NotNull BigDecimal geld);
}
