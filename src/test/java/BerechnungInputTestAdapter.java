import org.example.BerechnungInputPort;
import org.example.Geschäft;
import org.example.Konfiguration;

import java.util.List;

import static java.util.Collections.singletonList;

public class BerechnungInputTestAdapter implements BerechnungInputPort {
    private Geschäft g;
    private Konfiguration k;

    public BerechnungInputTestAdapter(Geschäft g, Konfiguration k) {
        this.g = g;
        this.k = k;
    }

    @Override
    public List<Geschäft> unberechneteGeschäfte(){
        return singletonList(g);
    }

    @Override
    public List<Konfiguration> konfigurationenFür(Geschäft geschäft) {
        return singletonList(k);
    }
}
