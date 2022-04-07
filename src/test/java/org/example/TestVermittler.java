package org.example;

import org.example.provisionsberechnung.Vermittler;

import java.util.UUID;

public class TestVermittler implements Vermittler {

    private String vermittlerNummer;

    public static TestVermittler defaultVermittler() {
        return new TestVermittler()
                .mitVermittlerNummer(UUID.randomUUID().toString());
    }

    @Override
    public String vermittlerNummer() {
        return this.vermittlerNummer;
    }

    public  TestVermittler mitVermittlerNummer(String vermittlerNummer) {
        this.vermittlerNummer = vermittlerNummer;
        return this;
    }
}
