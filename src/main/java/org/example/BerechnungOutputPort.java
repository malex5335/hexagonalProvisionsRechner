package org.example;

import java.math.BigDecimal;

public interface BerechnungOutputPort {
    void infoAnFreigebende(final BigDecimal summe);

    void fürFreigabeVorsehen(final Geschäft geschäft, final BigDecimal geld);

    void amPdfAnhängen(final Geschäft geschäft, final BigDecimal geld);

    void markiereBerechnet(final Geschäft geschäft);
}
