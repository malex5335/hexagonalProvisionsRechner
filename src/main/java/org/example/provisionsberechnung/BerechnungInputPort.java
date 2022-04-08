package org.example.provisionsberechnung;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BerechnungInputPort {

    /**
     * gibt alle Provisionen zurück, die für dieses Produkt gelten
     *
     * @param produkt dieses Produkt ist an der Provision konfiguriert
     * @return eine Liste
     */
    @NotNull
    List<Provision> alleProvisionen(@NotNull final Produkt produkt);

    /**
     * gibt alle Provisionen zurück, die für dieses Produkt und dieser Vermittler gelten
     *
     * @param produkt dieses Produkt ist an der Provision konfiguriert
     * @param vermittler dieser Vermittler ist an der Provision konfiguriert<br>
     *                   bei null wird nach Provisionen ohne Vermittler gesucht
     * @return eine Liste
     */
    @NotNull
    List<Provision> alleProvisionen(@NotNull final Produkt produkt, @NotNull final Vermittler vermittler);

    /**
     * gibt alle Produkte zurück, die das System kennt
     *
     * @return eine Liste
     */
    @NotNull
    List<Produkt> alleProdukte();

    /**
     * gibt alle Vermittler zurück, die das System kennt
     *
     * @return eine Liste
     */
    @NotNull
    List<Vermittler> alleVermittler();

    /**
     * gibt alle Geschäfte zurück, die zu diesem Produkt zugeordnet sind
     *
     * @param produkt dieses Produkt ist an dem Geschaeft konfiguriert
     * @return eine Liste
     */
    @NotNull
    List<Geschaeft> alleGeschaefte(@NotNull final Produkt produkt);

    /**
     * gibt alle Geschaefte zurück, die diesem Produkt und diesem Vermittler zugeordnet sind
     *
     * @param produkt dieses Produkt ist an dem Geschaeft konfiguriert
     * @param vermittler dieser Vermittler ist an dem Geschaeft konfiguriert<br>
     *                   bei null wird nach Provisionen ohne Vermittler gesucht
     * @return eine Liste
     */
    @NotNull
    List<Geschaeft> alleGeschaefte(@NotNull final Produkt produkt, @NotNull final Vermittler vermittler);
}
