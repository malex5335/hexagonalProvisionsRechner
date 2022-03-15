import org.example.BerechnungInputPort;
import org.example.Geschäft;
import org.example.Konfiguration;

import java.util.List;

public class BerechnungInputTestAdapter implements BerechnungInputPort {

    @Override
    public List<Geschäft> unberechneteGeschäfte() {
        return null;
    }

    @Override
    public List<Konfiguration> konfigurationenFür(Geschäft geschäft) {
        return null;
    }
}
