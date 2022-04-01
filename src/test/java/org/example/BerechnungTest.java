package org.example;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.example.TestGeschaeft.*;

public class BerechnungTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void berechnung_produktSpezifisch_1Geschaeft_erfolgreich() {
        // given
        var geschaefte = new ArrayList<Geschaeft>();
        var vermittler = new TestVermittler();
        var produkt = new TestProdukt();
        var konfiguration = new TestKonfiguration()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        var inputAdapter = new BerechnungInputTestAdapter()
                .mitVermittler(vermittler)
                .mitProukt(produkt)
                .mitKonfiguration(konfiguration)
                .mitGeschaeften(geschaefte);
        var outputAdapter = new BerechnungOutputTestAdapter();
        var berechnung = new Berechnung(inputAdapter, outputAdapter);
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
        var geschaefte = new ArrayList<Geschaeft>();
        var vermittler = new TestVermittler();
        var produkt = new TestProdukt();
        var konfiguration = new TestKonfiguration()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        var inputAdapter = new BerechnungInputTestAdapter()
                .mitVermittler(vermittler)
                .mitProukt(produkt)
                .mitKonfiguration(konfiguration)
                .mitGeschaeften(geschaefte);
        var outputAdapter = new BerechnungOutputTestAdapter();
        var berechnung = new Berechnung(inputAdapter, outputAdapter);
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
