package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BerechnungInputTestAdapter implements BerechnungInputPort {
    public List<Geschaeft> geschaefte = new ArrayList<>();
    public List<Konfiguration> konfigurationen = new ArrayList<>();
    public List<Produkt> produkte = new ArrayList<>();
    public List<Vermittler> vermittler_ = new ArrayList<>();

    public BerechnungInputTestAdapter mitGeschaeft(Geschaeft geschaeft) {
        geschaefte.add(geschaeft);
        return this;
    }

    public BerechnungInputTestAdapter mitGeschaeften(List<Geschaeft> geschaefte) {
        this.geschaefte = geschaefte;
        return this;
    }

    public BerechnungInputTestAdapter mitKonfiguration(Konfiguration konfiguration) {
        konfigurationen.add(konfiguration);
        return this;
    }

    public BerechnungInputTestAdapter mitProukt(Produkt produkt) {
        produkte.add(produkt);
        return this;
    }

    public BerechnungInputTestAdapter mitVermittler(Vermittler vermittler) {
        vermittler_.add(vermittler);
        return this;
    }

    @Override
    public List<Konfiguration> konfigurationenFuer(Produkt produkt) {
        return konfigurationen.stream()
                .filter(k -> k.fuerProdukt(produkt))
                .collect(Collectors.toList());
    }

    @Override
    public List<Konfiguration> konfigurationenFuer(Vermittler vermittler) {
        return konfigurationen.stream()
                .filter(k -> k.fuerVermittler(vermittler))
                .collect(Collectors.toList());
    }

    @Override
    public List<Produkt> alleProdukte() {
        return produkte;
    }

    @Override
    public List<Geschaeft> unberechneteGeschäfteFuerProdukt(Produkt produkt) {
        return geschaefte.stream()
                .filter(g -> g.fuerProdukt(produkt))
                .collect(Collectors.toList());
    }

    @Override
    public List<Vermittler> alleVermittler() {
        return vermittler_;
    }

    @Override
    public List<Geschaeft> unberechneteGeschaefteFuerVermittler(Vermittler vermittler) {
        return geschaefte.stream()
                .filter(g -> konfigurationen.stream()
                        .anyMatch(g::istBerechnetFuerKonfiguration))
                .filter(g -> g.fuerVermittler(vermittler))
                .collect(Collectors.toList());
    }
}
