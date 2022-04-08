package org.example.provisionsberechnung;

import java.util.List;

public interface BerechnungInputPort {
    List<Provision> alleProvisionen(final Produkt produkt);

    List<Provision> alleProvisionen(final Produkt produkt, final Vermittler vermittler);

    List<Produkt> alleProdukte();

    List<Geschaeft> unberechneteGeschaefte(Produkt produkt);

    List<Vermittler> alleVermittler();

    List<Geschaeft> unberechneteGeschaefte(final Vermittler vermittler, final Produkt produkt);
}
