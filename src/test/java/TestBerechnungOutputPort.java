import org.example.BerechnungOutputPort;
import org.example.Geschäft;

import java.math.BigDecimal;

public class TestBerechnungOutputPort implements BerechnungOutputPort {

    @Override
    public void infoAnFreigebende(BigDecimal summe) {

    }

    @Override
    public void fürFreigabeVorsehen(Geschäft geschäft, BigDecimal geld) {

    }

    @Override
    public void amPdfAnhängen(Geschäft geschäft, BigDecimal geld) {

    }

    @Override
    public void markiereBerechnet(Geschäft geschäft) {

    }
}
