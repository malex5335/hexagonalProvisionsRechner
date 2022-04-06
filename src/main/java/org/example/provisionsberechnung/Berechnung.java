package org.example.provisionsberechnung;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

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
            for(var konfiguration : berechnungInputPort.konfigurationenFuer(produkt)) {
                var geschaefte = berechnungInputPort.unberechneteGeschaefteFuerProdukt(produkt);
                var geld = konfiguration.berechneGeld(geschaefte, this::sollBerechnetWerden);
                berechnungOutputPort.markiereBerechnet(geschaefte, konfiguration);
                summe = summe.add(geld);
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
            for(var konfiguration : berechnungInputPort.konfigurationenFuer(vermittler)) {
                var geschaefte = berechnungInputPort.unberechneteGeschaefteFuerVermittler(vermittler);
                var geld = konfiguration.berechneGeld(geschaefte, this::sollBerechnetWerden);
                berechnungOutputPort.markiereBerechnet(geschaefte, konfiguration);
                summe = summe.add(geld);
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
}
