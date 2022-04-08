package org.example.provisionsberechnung;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public interface Geschaeft {

    /**
     * dieses Datum gibt an, wann das Geschaeft angeliefert wurde
     *
     * @return ein Datum mit Uhrzeitangabe
     */
    @NotNull
    LocalDateTime anlieferDatum();

    /**
     * gibt an, ob das Geschaeft diesem Vermittler zugeordnet ist
     *
     * @param vermittler der Vermittler mit dem geprüft wird
     * @return true || false
     */
    boolean fuerVermittler(@NotNull Vermittler vermittler);

    /**
     * gibt an, ob das Geschaeft diesem Produkt zugeordnet ist
     *
     * @param produkt das Produkt mit dem geprüft wird
     * @return true || false
     */
    boolean fuerProdukt(@NotNull Produkt produkt);

    enum Status {
        SALE, LEAD
    }

    /**
     * gibt den aktuellen {@link Status} dieses Geschaeftes zurück
     *
     * @return einen Wert aus {@link Status}
     */
    @NotNull
    Status status();

    /**
     * gibt eine Map aller volumenbeträge zurück<br>
     * der key ist hierbei der Feldbezeichner, der Value stellt den Wert da
     *
     * @return eine Map
     */
    @NotNull
    Map<String, BigDecimal> volumenBetraege();

    /**
     * gibt an, ob dieses Geschaeft bereits für diese Provision berechnet wurde
     *
     * @param provision diese Provision wird geprüft
     * @return true || false
     */
    boolean istBerechnetFuerProvision(@Nullable Provision provision);
}
