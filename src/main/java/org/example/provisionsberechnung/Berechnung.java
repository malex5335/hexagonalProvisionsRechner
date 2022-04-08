package org.example.provisionsberechnung;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Berechnung {
    public BerechnungInputPort berechnungInputPort;
    public BerechnungOutputPort berechnungOutputPort;

    public Berechnung(BerechnungInputPort berechnungInputPort, BerechnungOutputPort berechnungOutputPort) {
        this.berechnungInputPort = berechnungInputPort;
        this.berechnungOutputPort = berechnungOutputPort;
    }

    /**
     * berechnet produktspezifische Konfigurationen
     * ausgezahltes Geld wird addiert und mittels
     * {@link BerechnungOutputPort#infoAnFreigebende(BigDecimal)} weiterverarbeitet
     */
    public void berechneProduktSpezifischeKonfigs() {
        var summe = BigDecimal.ZERO;
        for(var produkt : berechnungInputPort.alleProdukte()) {
            if(produkt.istAktiv()) {
                for (var konfiguration : berechnungInputPort.alleKonfigurationen(produkt)) {
                    var geschaefte = berechnungInputPort.unberechneteGeschaefte(produkt);
                    summe = summe.add(berechneGeschaefte(geschaefte, konfiguration));
                }
            }
        }
        berechnungOutputPort.infoAnFreigebende(summe);
    }

    /**
     * berechnet vermittlerspezifische Konfigurationen für Haupt- und Untervermittler
     * ausgezahltes Geld wird addiert und mittels
     * {@link BerechnungOutputPort#infoAnFreigebende(BigDecimal)} weiterverarbeitet
     */
    public void berechneVermittlerSpezifischeKonfigs() {
        var summe = BigDecimal.ZERO;
        for(var vermittler : berechnungInputPort.alleVermittler()) {
            var hauptVermittler = vermittler.hauptVermittler();
            summe = summe.add(berechneFuerVermittler(Objects.requireNonNullElse(hauptVermittler, vermittler), vermittler));
        }
        berechnungOutputPort.infoAnFreigebende(summe);
    }

    /**
     * berechnet vermittlerspezifische Konfigurationen
     * ausgezahltes Geld wird addiert
     *
     * @param ausKonfiguration  der Vermittler dessen Konfiguration berechnet werden
     * @param ausGeschaeften    der Vermittler dessen Geschaefte berechnet werden
     * @return  eine Summe an Geld, welche berechnet werde<br>
     *          als Standard wird {@link BigDecimal.ZERO} zurückgegeben
     */
    private BigDecimal berechneFuerVermittler(Vermittler ausKonfiguration, Vermittler ausGeschaeften) {
        var summe = BigDecimal.ZERO;
        for(var produkt : berechnungInputPort.alleProdukte()) {
            if(produkt.istAktiv()) {
                for (var konfiguration : berechnungInputPort.alleKonfigurationen(produkt, ausKonfiguration)) {
                    var geschaefte = berechnungInputPort.unberechneteGeschaefte(ausGeschaeften, produkt);
                    summe = summe.add(berechneGeschaefte(geschaefte, konfiguration));
                }
            }
        }
        return summe;
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

    private BigDecimal berechneGeschaefte(List<Geschaeft> geschaefte, Konfiguration konfiguration) {
        var summe = BigDecimal.ZERO;
        for(var geschaeft : geschaefte) {
            if(sollBerechnetWerden(geschaeft)) {
                var geld = konfiguration.berechneGeld(geschaeft);
                berechnungOutputPort.markiereBerechnet(geschaefte, konfiguration);
                summe = summe.add(geld);
            }
        }
        return summe;
    }
}
