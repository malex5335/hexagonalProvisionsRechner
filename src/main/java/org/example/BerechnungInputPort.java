package org.example;

import java.util.Collections;
import java.util.List;

public interface BerechnungInputPort {

    default List<Geschäft> unberechneteGeschäfte() {
        return Collections.emptyList();
    }

    default List<Konfiguration> konfigurationenFür(Geschäft geschäft) {
        return Collections.emptyList();
    }
}
