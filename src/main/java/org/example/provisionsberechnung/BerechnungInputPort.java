package org.example.provisionsberechnung;

import java.util.List;

public interface BerechnungInputPort {
    List<Konfiguration> konfigurationenFuer(final Produkt produkt);

    List<Konfiguration> konfigurationenFuer(final Vermittler vermittler);

    List<Produkt> alleProdukte();

    List<Geschaeft> unberechneteGeschaefteFuerProdukt(Produkt produkt);

    List<Vermittler> alleVermittler();

    List<Geschaeft> unberechneteGeschaefteFuerVermittler(Vermittler vermittler);
}
