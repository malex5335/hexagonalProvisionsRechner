import org.example.Geschäft;
import org.example.Produkt;
import org.example.Vermittler;

import java.time.LocalDateTime;

public class TestGeschäft implements Geschäft {
    private LocalDateTime a;
    private Produkt p;
    private Vermittler v;
    private Status s;

    public TestGeschäft mitAnlieferDatum(LocalDateTime a) {
        this.a = a;
        return this;
    }

    public TestGeschäft fürProdukt(Produkt p) {
        this.p = p;
        return this;
    }

    public TestGeschäft fürVermittler(Vermittler v) {
        this.v = v;
        return this;
    }

    public TestGeschäft mitStatus(Status s) {
        this.s = s;
        return this;
    }

    @Override
    public LocalDateTime anlieferDatum() {
        return a;
    }

    @Override
    public Produkt produkt() {
        return p;
    }

    @Override
    public Vermittler vermittler() {
        return v;
    }

    @Override
    public Status status() {
        return s;
    }
}
