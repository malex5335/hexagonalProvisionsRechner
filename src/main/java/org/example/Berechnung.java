package org.example;

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

    public void berechneGeschäfte() {
        for(var g : berechnungInputPort.unberechneteGeschäfte()) {
            if(sollBerechnetWerden(g)) {
                var summe = BigDecimal.ZERO;
                for(var k : berechnungInputPort.konfigurationenFür(g)) {
                    var geld = k.berechneGeld(g);
                    summe = summe.add(geld);
                    berechnungOutputPort.fürFreigabeVorsehen(g, geld);
                    if(geld.compareTo(BigDecimal.ZERO) != 0) {
                        berechnungOutputPort.amPdfAnhängen(g, geld);
                    }
                    berechnungOutputPort.markiereBerechnet(g);
                }
                berechnungOutputPort.infoAnFreigebende(summe);
            }
        }
    }

    private boolean sollBerechnetWerden(Geschäft geschäft){
        return geschäft.status().equals(Geschäft.Status.SALE) &&
                inDerVergangenheit(Duration.ofDays(120), geschäft.anlieferDatum()) &&
                geschäft.produkt().aktiv(geschäft.vermittler());
    }

    private boolean inDerVergangenheit(Duration alter, LocalDateTime zeitpunkt) {
        var heute = LocalDateTime.now();
        var verganenheit = heute.minus(alter);
        return zeitpunkt.isBefore(verganenheit);
    }
}
