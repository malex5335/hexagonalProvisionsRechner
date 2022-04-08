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
     * berechnet produktspezifische Provisionen
     * ausgezahltes Geld wird addiert und mittels
     * {@link BerechnungOutputPort#infoAnFreigebende(BigDecimal)} weiterverarbeitet
     */
    public void berechneProduktSpezifischeProvisioenn() {
        var summe = BigDecimal.ZERO;
        for(var produkt : berechnungInputPort.alleProdukte()) {
            if(produkt.istAktiv()) {
                for (var provision : berechnungInputPort.alleProvisionen(produkt)) {
                    var geschaefte = berechnungInputPort.unberechneteGeschaefte(produkt);
                    summe = summe.add(berechneGeschaefte(geschaefte, provision));
                }
            }
        }
        berechnungOutputPort.infoAnFreigebende(summe);
    }

    /**
     * berechnet vermittlerspezifische Provisionen für Haupt- und Untervermittler
     * ausgezahltes Geld wird addiert und mittels
     * {@link BerechnungOutputPort#infoAnFreigebende(BigDecimal)} weiterverarbeitet
     */
    public void berechneVermittlerSpezifischeProvisionen() {
        var summe = BigDecimal.ZERO;
        for(var vermittler : berechnungInputPort.alleVermittler()) {
            var hauptVermittler = vermittler.hauptVermittler();
            summe = summe.add(berechneFuerVermittler(Objects.requireNonNullElse(hauptVermittler, vermittler), vermittler));
        }
        berechnungOutputPort.infoAnFreigebende(summe);
    }

    /**
     * berechnet vermittlerspezifische Provisionen
     * ausgezahltes Geld wird addiert
     *
     * @param ausProvision  der Vermittler dessen Provision berechnet werden
     * @param ausGeschaeften    der Vermittler dessen Geschaefte berechnet werden
     * @return  eine Summe an Geld, welche berechnet werde<br>
     *          als Standard wird {@link BigDecimal.ZERO} zurückgegeben
     */
    private BigDecimal berechneFuerVermittler(Vermittler ausProvision, Vermittler ausGeschaeften) {
        var summe = BigDecimal.ZERO;
        for(var produkt : berechnungInputPort.alleProdukte()) {
            if(produkt.istAktiv()) {
                for (var provision : berechnungInputPort.alleProvisionen(produkt, ausProvision)) {
                    var geschaefte = berechnungInputPort.unberechneteGeschaefte(ausGeschaeften, produkt);
                    summe = summe.add(berechneGeschaefte(geschaefte, provision));
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

    private BigDecimal berechneGeschaefte(List<Geschaeft> geschaefte, Provision provision) {
        var summe = BigDecimal.ZERO;
        for(var geschaeft : geschaefte) {
            if(sollBerechnetWerden(geschaeft)) {
                var geld = provision.berechneGeld(geschaeft);
                berechnungOutputPort.markiereBerechnet(geschaefte, provision);
                summe = summe.add(geld);
            }
        }
        return summe;
    }
}
