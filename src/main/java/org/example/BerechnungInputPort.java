package org.example;

import java.util.List;

public interface BerechnungInputPort {
    List<Konfiguration> konfigurationenFuer(final Produkt produkt);

    List<Konfiguration> konfigurationenFuer(final Vermittler vermittler);

    List<Produkt> alleProdukte();

    List<Geschaeft> unberechneteGeschäfteFuerProdukt(Produkt produkt);

    List<Vermittler> alleVermittler();

    List<Geschaeft> unberechneteGeschaefteFuerVermittler(Vermittler vermittler);
}
