import org.example.BerechnungOutputPort;
import org.example.Geschäft;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class BerechnungOutputTestAdapter implements BerechnungOutputPort {
    private Geschäft g;
    private BigDecimal summe;
    private Map<Geschäft, BigDecimal> geldProGeschäft;

    public BerechnungOutputTestAdapter(Geschäft g) {
        this.g = g;
        geldProGeschäft = new HashMap<>();
    }

    public void mitSumme(BigDecimal summe) {
        this.summe = summe;
    }

    public void mitGeldFürGeschäft(Geschäft geschäft, BigDecimal geld) {
        this.geldProGeschäft.put(geschäft, geld);
    }

    @Override
    public void infoAnFreigebende(BigDecimal summe) {
        assertThat(summe).isEqualByComparingTo(this.summe);
    }

    @Override
    public void fürFreigabeVorsehen(Geschäft geschäft, BigDecimal geld) {
        assertThat(geld).isEqualByComparingTo(this.geldProGeschäft.get(geschäft));
    }

    @Override
    public void amPdfAnhängen(Geschäft geschäft, BigDecimal geld) {
        assertThat(geld).isEqualByComparingTo(this.geldProGeschäft.get(geschäft));
    }

    @Override
    public void markiereBerechnet(Geschäft geschäft) {
        assertThat(this.geldProGeschäft.containsKey(geschäft)).isTrue();
    }
}
