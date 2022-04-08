package org.example;

import org.example.provisionsberechnung.Produkt;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TestProdukt implements Produkt {

    private String produktName;
    private boolean aktiv;

    public static TestProdukt defaultProdukt() {
        return new TestProdukt()
                .mitProduktName(UUID.randomUUID().toString())
                .mitAktiv(true);
    }

    @Override
    public @NotNull String produktName() {
        return produktName;
    }

    @Override
    public boolean istJetztAktiv() {
        return aktiv;
    }

    public TestProdukt mitProduktName(String produktName) {
        this.produktName = produktName;
        return this;
    }

    public TestProdukt mitAktiv(boolean aktiv) {
        this.aktiv = aktiv;
        return this;
    }
}
