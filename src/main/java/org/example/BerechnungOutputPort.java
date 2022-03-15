package org.example;

import java.math.BigDecimal;

public interface BerechnungOutputPort {
    void infoAnFreigebende(BigDecimal summe);

    void fürFreigabeVorsehen(Geschäft geschäft, BigDecimal geld);

    void amPdfAnhängen(Geschäft geschäft, BigDecimal geld);

    void markiereBerechnet(Geschäft geschäft);
}
