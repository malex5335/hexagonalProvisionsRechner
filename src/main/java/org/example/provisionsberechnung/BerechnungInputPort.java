package org.example.provisionsberechnung;

import java.util.List;

public interface BerechnungInputPort {
    List<Konfiguration> alleKonfigurationen(final Produkt produkt);

    List<Konfiguration> alleKonfigurationen(final Produkt produkt, final Vermittler vermittler);

    List<Produkt> alleProdukte();

    List<Geschaeft> unberechneteGeschaefte(Produkt produkt);

    List<Vermittler> alleVermittler();

    List<Geschaeft> unberechneteGeschaefte(final Vermittler vermittler, final Produkt produkt);
}
