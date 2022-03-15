package org.example;

import java.util.List;

public interface BerechnungInputPort {

    List<Geschäft> unberechneteGeschäfte();

    List<Konfiguration> konfigurationenFür(Geschäft geschäft);
}
