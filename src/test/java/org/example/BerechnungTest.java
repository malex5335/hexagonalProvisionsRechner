package org.example;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.example.TestGeschaeft.*;

public class BerechnungTest {
    private Vermittler vermittler;
    private Produkt produkt;
    private final List<Geschaeft> geschaefte = new ArrayList<>();
    private TestKonfiguration konfiguration;
    private BerechnungOutputTestAdapter outputAdapter;
    private Berechnung berechnung;

    @BeforeEach
    void setUp() {
        vermittler = new TestVermittler();
        produkt = new TestProdukt();
        konfiguration = new TestKonfiguration()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        var inputAdapter = new BerechnungInputTestAdapter()
                .mitVermittler(vermittler)
                .mitProukt(produkt)
                .mitKonfiguration(konfiguration)
                .mitGeschaeften(geschaefte);
        outputAdapter = new BerechnungOutputTestAdapter();
        berechnung = new Berechnung(inputAdapter, outputAdapter);
    }

    @AfterEach
    void tearDown() {
        geschaefte.clear();
    }

    @Test
    public void berechnung_produktSpezifisch_1Geschaeft_erfolgreich() {
        // given
        var geld = new BigDecimal(10);
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        geschaefte.add(geschaeft);
        konfiguration
                .mitGeldFuerGeschaeft(geschaeft, geld);

        // when
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertEquals(0, outputAdapter.summe.compareTo(geld));
        assertTrue(geschaeft.istBerechnetFuerKonfiguration(konfiguration));
    }

    @Test
    public void berechnung_produktSpezifisch_2Geschaefte_erfolgreich() {
        // given
        var geld = new BigDecimal(10);
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        var geschaeft2 = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        geschaefte.add(geschaeft);
        geschaefte.add(geschaeft2);
        konfiguration
                .mitGeldFuerGeschaeft(geschaeft, geld)
                .mitGeldFuerGeschaeft(geschaeft2, geld);

        // when
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertEquals(0, outputAdapter.summe.compareTo(new BigDecimal(20)));
        assertTrue(geschaeft.istBerechnetFuerKonfiguration(konfiguration));
        assertTrue(geschaeft2.istBerechnetFuerKonfiguration(konfiguration));
    }
}
