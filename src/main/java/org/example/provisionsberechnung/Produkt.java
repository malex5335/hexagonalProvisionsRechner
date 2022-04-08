package org.example.provisionsberechnung;

import org.jetbrains.annotations.NotNull;

public interface Produkt {

    /**
     * @return der eindeutige Name dieses Produkts
     */
    @NotNull
    String produktName();

    /**
     * @return true, wenn dieses Produkt jetzt aktiv ist, ansonsten false
     */
    boolean istJetztAktiv();
}
