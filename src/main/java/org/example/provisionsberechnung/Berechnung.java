package org.example.provisionsberechnung;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
     * berechnet vermittlerspezifische Konfigurationen
     * ausgezahltes Geld wird addiert und mittels
     * {@link BerechnungOutputPort#infoAnFreigebende(BigDecimal)} weiterverarbeitet
     */
    public void berechneVermittlerSpezifischeKonfigs() {
        var summe = BigDecimal.ZERO;
        for(var vermittler : berechnungInputPort.alleVermittler()) {
            for(var produkt : berechnungInputPort.alleProdukte()) {
                if(produkt.istAktiv()) {
                    for (var konfiguration : berechnungInputPort.alleKonfigurationen(produkt, vermittler)) {
                        var geschaefte = berechnungInputPort.unberechneteGeschaefte(vermittler, produkt);
                        summe = summe.add(berechneGeschaefte(geschaefte, konfiguration));
                    }
                }
            }
        }
        berechnungOutputPort.infoAnFreigebende(summe);
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
