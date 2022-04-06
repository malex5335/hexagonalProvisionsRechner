package org.example;

import org.example.provisionsberechnung.Produkt;

import java.util.UUID;

public class TestProdukt implements Produkt {

    private String produktName = UUID.randomUUID().toString();

    @Override
    public String produktName() {
        return produktName;
    }

    public TestProdukt mitProduktName(String produktName) {
        this.produktName = produktName;
        return this;
    }
}
