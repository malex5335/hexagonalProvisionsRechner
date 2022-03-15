package org.example;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class Berechnung {

    public void berechneGeschäfte() {
        for(var g : unberechneteGeschäfte()) {
            if(berechnenOk(g)) {
                var summe = BigDecimal.ZERO;
                for(var k : konfigurationenFür(g)) {
                    var geld = k.berechneGeld(g);
                    summe = summe.add(geld);
                    fürFreigabeVorsehen(g, geld);
                    if(geld.compareTo(BigDecimal.ZERO) != 0) {
                        amPdfAnhängen(g, geld);
                    }
                    g.markiereBerechnet(true);
                }
                infoAnFreigebende(summe);
            }
        }
    }

    public boolean berechnenOk(Geschäft geschäft){
        return geschäft.status().equals(Geschäft.Status.SALE) &&
                inDerVergangenheit(Duration.ofDays(120), geschäft.anlieferDatum()) &&
                geschäft.produkt().aktiv(geschäft.vermittler());
    }

    public List<Geschäft> unberechneteGeschäfte() {
        // give me that stuff
        return Collections.emptyList();
    }

    public List<Konfiguration> konfigurationenFür(Geschäft geschäft) {
        // give me that stuff
        return Collections.emptyList();
    }

    public void infoAnFreigebende(BigDecimal summe) {
		//...
    }

    public void fürFreigabeVorsehen(Geschäft geschäft, BigDecimal geld) {
		//...
    }

    public void amPdfAnhängen(Geschäft geschäft, BigDecimal geld) {
		//...
    }

    public boolean inDerVergangenheit(Duration alter, LocalDateTime zeitpunkt) {
        var heute = LocalDateTime.now();
        var verganenheit = heute.minus(alter);
        return zeitpunkt.isBefore(verganenheit);
    }
}
