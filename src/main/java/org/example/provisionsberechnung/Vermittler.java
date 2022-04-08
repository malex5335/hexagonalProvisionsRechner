package org.example.provisionsberechnung;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Vermittler {

    /**
     * @return die eindeutige Nummer des Vermittlers
     */
    @NotNull
    String vermittlerNummer();

    /**
     * gibt den Hauptvermittler an, dem dieser Vermittler untergeordnet ist<br>
     * kann null sein
     *
     * @return der übergeordnete Vermittler oder null, wenn es diesen nicht gibt
     */
    @Nullable
    Vermittler hauptVermittler();
}
