package org.example;

import org.example.provisionsberechnung.Vermittler;

import java.util.UUID;

public class TestVermittler implements Vermittler {

    private String vermittlerNummer;
    private Vermittler haupVermittler;

    public static TestVermittler defaultVermittler() {
        return new TestVermittler()
                .mitVermittlerNummer(UUID.randomUUID().toString());
    }

    @Override
    public String vermittlerNummer() {
        return this.vermittlerNummer;
    }

    @Override
    public Vermittler hauptVermittler() {
        return this.haupVermittler;
    }

    public  TestVermittler mitVermittlerNummer(String vermittlerNummer) {
        this.vermittlerNummer = vermittlerNummer;
        return this;
    }

    public TestVermittler mitHauptVermittler(Vermittler vermittler) {
        this.haupVermittler = vermittler;
        return this;
    }
}
