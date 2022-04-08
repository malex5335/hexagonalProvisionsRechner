package org.example.provisionsberechnung;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

public interface Provision {

    /**
     * berechnet das Geld für dieses Geschaeft und gibt das Ergebnis zurück
     *
     * @param geschaeft dieses Geschaeft soll berechnet werden
     * @return einen Betrag
     */
    @NotNull
    BigDecimal berechneGeld(@NotNull Geschaeft geschaeft);

    /**
     * gibt an, ob diese Provision für dieses Produkt gilt
     *
     * @param produkt dieses Produkt wird geprüft
     * @return true || false
     */
    boolean fuerProdukt(@NotNull Produkt produkt);

    /**
     * gibt an, ob diese Provision für diesen Vermitter gilt
     *
     * @param vermittler dieser Vermittler wird geprüft
     * @return true || false
     */
    boolean fuerVermittler(@NotNull Vermittler vermittler);
}
