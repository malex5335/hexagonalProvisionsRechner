package org.example;

import org.example.provisionsberechnung.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BerechnungInputTestAdapter implements BerechnungInputPort {
    public List<Geschaeft> geschaefte = new ArrayList<>();
    public List<Konfiguration> konfigurationen = new ArrayList<>();
    public List<Produkt> produkte = new ArrayList<>();
    public List<Vermittler> vermittler_ = new ArrayList<>();

    public BerechnungInputTestAdapter mitGeschaeften(List<Geschaeft> geschaefte) {
        this.geschaefte = geschaefte;
        return this;
    }

    public BerechnungInputTestAdapter mitKonfigurationen(List<Konfiguration> konfiguration) {
        konfigurationen = konfiguration;
        return this;
    }

    public BerechnungInputTestAdapter mitProukten(List<Produkt> produkt) {
        produkte = produkt;
        return this;
    }

    public BerechnungInputTestAdapter mitVermittler(List<Vermittler> vermittler) {
        vermittler_ = vermittler;
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
    public List<Geschaeft> unberechneteGeschaefteFuerProdukt(Produkt produkt) {
        return geschaefte.stream()
                .filter(g -> g.fuerProdukt(produkt))
                .filter(g -> konfigurationen.stream()
                        .noneMatch(g::istBerechnetFuerKonfiguration))
                .collect(Collectors.toList());
    }

    @Override
    public List<Vermittler> alleVermittler() {
        return vermittler_;
    }

    @Override
    public List<Geschaeft> unberechneteGeschaefteFuerVermittler(Vermittler vermittler) {
        var alleGeschaefte = geschaefte.stream()
                .filter(g -> g.fuerVermittler(vermittler))
                .filter(g -> konfigurationen.stream()
                        .noneMatch(g::istBerechnetFuerKonfiguration))
                .collect(Collectors.toList());

        return alleGeschaefte;
    }
}
