package org.example;

import org.example.provisionsberechnung.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BerechnungInputTestAdapter implements BerechnungInputPort {
    public List<Geschaeft> geschaefte = new ArrayList<>();
    public List<Provision> provisionen = new ArrayList<>();
    public List<Produkt> produkte = new ArrayList<>();
    public List<Vermittler> vermittler_ = new ArrayList<>();

    public BerechnungInputTestAdapter mitGeschaeften(List<Geschaeft> geschaefte) {
        this.geschaefte = geschaefte;
        return this;
    }

    public BerechnungInputTestAdapter mitProvisionen(List<Provision> provision) {
        provisionen = provision;
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
    public List<Provision> alleProvisionen(Produkt produkt) {
        return provisionen.stream()
                .filter(k -> k.fuerProdukt(produkt))
                .collect(Collectors.toList());
    }

    @Override
    public List<Provision> alleProvisionen(Produkt produkt, Vermittler vermittler) {
        return provisionen.stream()
                .filter(k -> k.fuerProdukt(produkt))
                .filter(k -> k.fuerVermittler(vermittler))
                .collect(Collectors.toList());
    }

    @Override
    public List<Produkt> alleProdukte() {
        return produkte;
    }

    @Override
    public List<Vermittler> alleVermittler() {
        return vermittler_;
    }

    @Override
    public List<Geschaeft> alleGeschaefte(Produkt produkt) {
        return geschaefte.stream()
                .filter(g -> g.fuerProdukt(produkt))
                .collect(Collectors.toList());
    }

    @Override
    public List<Geschaeft> alleGeschaefte(Produkt produkt, Vermittler vermittler) {
        return geschaefte.stream()
                .filter(g -> g.fuerProdukt(produkt))
                .filter(g -> g.fuerVermittler(vermittler))
                .collect(Collectors.toList());
    }
}
