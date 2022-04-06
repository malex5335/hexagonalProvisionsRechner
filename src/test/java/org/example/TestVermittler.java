package org.example;

import org.example.provisionsberechnung.Vermittler;

import java.util.UUID;

public class TestVermittler implements Vermittler {

    private String vermittlerNummer = UUID.randomUUID().toString();

    @Override
    public String vermittlerNummer() {
        return this.vermittlerNummer;
    }

    public  TestVermittler mitVermittlerNummer(String vermittlerNummer) {
        this.vermittlerNummer = vermittlerNummer;
        return this;
    }
}
