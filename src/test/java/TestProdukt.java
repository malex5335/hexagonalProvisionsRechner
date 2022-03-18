import org.example.Produkt;
import org.example.Vermittler;

public class TestProdukt implements Produkt {
    @Override
    public boolean aktiv(Vermittler vermittler) {
        return false;
    }
}
