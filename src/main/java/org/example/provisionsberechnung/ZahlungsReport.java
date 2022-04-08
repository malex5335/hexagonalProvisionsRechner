package org.example.provisionsberechnung;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

public interface ZahlungsReport {

    /**
     * gibt die berechneten Gescheafte in diesem Zahlungsreport zurück
     *
     * @return eine Liste
     */
    @NotNull
    List<Geschaeft> berechneteGeschaefte();

    /**
     * gibt die verwendeten Provisionen für das angegebene Geschäft dieses Zahlungsreports zurück
     *
     * @param geschaeft für dieses Geschäft werden Provisionen gesucht
     * @return eine Liste
     */
    @NotNull
    List<Provision> provisionenFuerGeschaeft(Geschaeft geschaeft);

    /**
     * gibt die Beträge für das Geschaeft in dieser Provision für den Zahlungsreport zurück
     *
     * @param geschaeft für dieses Geschaeft wird nach dem Betrag gesucht
     * @param provision für diese Provision wird nach dem Betrag gesucht
     * @return eine Zahl, mindestens {@link BigDecimal#ZERO}
     */
    @NotNull
    BigDecimal betragFuerGeschaeft(Geschaeft geschaeft, Provision provision);
}
