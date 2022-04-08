package org.example.provisionsberechnung;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Berechnung {
    public BerechnungInputPort berechnungInputPort;
    public BerechnungOutputPort berechnungOutputPort;

    public Berechnung(BerechnungInputPort berechnungInputPort, BerechnungOutputPort berechnungOutputPort) {
        this.berechnungInputPort = berechnungInputPort;
        this.berechnungOutputPort = berechnungOutputPort;
    }

    /**
     * berechnet produktspezifische Provisionen
     * ausgezahltes Geld wird addiert und mittels
     * {@link BerechnungOutputPort#sendeBerechnungsInfos(Map)} weiterverarbeitet
     */
    public void berechneProduktSpezifischeProvisionen() {
        Map<Vermittler, BigDecimal> geldFuerVermittler = new HashMap<>();
        for(var produkt : berechnungInputPort.alleProdukte()) {
            if(produkt.istJetztAktiv()) {
                for (var provision : berechnungInputPort.alleProvisionen(produkt)) {
                    var geschaefteFuerVermittler = berechnungInputPort.alleGeschaefte(produkt).stream()
                            .filter(g -> !g.istBerechnetFuerProvision(provision))
                            .collect(Collectors.groupingBy(Geschaeft::vermittler));
                    for(var vermittler : geschaefteFuerVermittler.keySet()) {
                        var geschaefte = geschaefteFuerVermittler.get(vermittler);
                        geldFuerVermittler.put(vermittler, berechneGeschaefte(vermittler, geschaefte, provision));
                    }
                }
            }
        }
        berechnungOutputPort.sendeBerechnungsInfos(geldFuerVermittler);
    }

    /**
     * berechnet vermittlerspezifische Provisionen für Haupt- und Untervermittler
     * ausgezahltes Geld wird addiert und mittels
     * {@link BerechnungOutputPort#sendeBerechnungsInfos(Map)} weiterverarbeitet
     */
    public void berechneVermittlerSpezifischeProvisionen() {
        Map<Vermittler, BigDecimal> geldFuerVermittler = new HashMap<>();
        for(var geschaeftsVermittler : berechnungInputPort.alleVermittler()) {
            var hauptVermittler = geschaeftsVermittler.hauptVermittler();
            var provisionsVermittler = Objects.requireNonNullElse(hauptVermittler, geschaeftsVermittler);
            for(var produkt : berechnungInputPort.alleProdukte()) {
                if(produkt.istJetztAktiv()) {
                    for (var provision : berechnungInputPort.alleProvisionen(produkt, provisionsVermittler)) {
                        var geschaefte = berechnungInputPort.alleGeschaefte(produkt, geschaeftsVermittler).stream()
                                .filter(g -> !g.istBerechnetFuerProvision(provision))
                                .collect(Collectors.toList());
                        geldFuerVermittler.put(geschaeftsVermittler, berechneGeschaefte(geschaeftsVermittler, geschaefte, provision));
                    }
                }
            }
            berechnungOutputPort.sendeBerechnungsInfos(geldFuerVermittler);
        }
    }

    private boolean sollBerechnetWerden(Geschaeft geschaeft){
        return geschaeft.status().equals(Geschaeft.Status.SALE) &&
                inDerVergangenheit(Duration.ofDays(120), geschaeft.anlieferDatum());
    }

    private boolean inDerVergangenheit(Duration alter, LocalDateTime zeitpunkt) {
        var heute = LocalDateTime.now();
        var verganenheit = heute.minus(alter);
        return zeitpunkt.isBefore(verganenheit);
    }

    private BigDecimal berechneGeschaefte(Vermittler vermittler, List<Geschaeft> geschaefte, Provision provision) {
        var summe = BigDecimal.ZERO;
        for(var geschaeft : geschaefte) {
            summe = summe.add(berechneGeschaeft(vermittler, geschaeft, provision));
        }
        return summe;
    }

    private BigDecimal berechneGeschaeft(Vermittler vermittler, Geschaeft geschaeft, Provision provision) {
        if(sollBerechnetWerden(geschaeft)) {
            var geld = provision.berechneGeld(geschaeft);
            if(vermittler.ergaenzeInZahlungsReport(geschaeft, provision, geld)) {
                berechnungOutputPort.markiereBerechnet(geschaeft, provision);
            }
            return geld;
        }
        return BigDecimal.ZERO;
    }
}
