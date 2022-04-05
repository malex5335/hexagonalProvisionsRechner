package org.example;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
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
        var geld = new BigDecimal(10);
        var geschaefte = new ArrayList<Geschaeft>();
        var vermittler = new TestVermittler();
        var produkt = new TestProdukt();
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        geschaefte.add(geschaeft);
        var konfiguration = new TestKonfiguration()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        konfiguration
                .mitGeldFuerGeschaeft(geschaeft, geld);
        var inputAdapter = new BerechnungInputTestAdapter()
                .mitVermittler(vermittler)
                .mitProukt(produkt)
                .mitKonfiguration(konfiguration)
                .mitGeschaeften(geschaefte);
        var outputAdapter = new BerechnungOutputTestAdapter();
        var berechnung = new Berechnung(inputAdapter, outputAdapter);

        // when
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(geld);
        assertThat(geschaeft.istBerechnetFuerKonfiguration(konfiguration)).isTrue();
    }

    @Test
    public void berechnung_produktSpezifisch_2Geschaefte_erfolgreich() {
        // given
        var geld = new BigDecimal(10);
        var geschaefte = new ArrayList<Geschaeft>();
        var vermittler = new TestVermittler();
        var produkt = new TestProdukt();
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        var geschaeft2 = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        geschaefte.add(geschaeft);
        geschaefte.add(geschaeft2);
        var konfiguration = new TestKonfiguration()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        konfiguration
                .mitGeldFuerGeschaeft(geschaeft, geld)
                .mitGeldFuerGeschaeft(geschaeft2, geld);
        var inputAdapter = new BerechnungInputTestAdapter()
                .mitVermittler(vermittler)
                .mitProukt(produkt)
                .mitKonfiguration(konfiguration)
                .mitGeschaeften(geschaefte);
        var outputAdapter = new BerechnungOutputTestAdapter();
        var berechnung = new Berechnung(inputAdapter, outputAdapter);

        // when
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(new BigDecimal(20));
        assertThat(geschaeft.istBerechnetFuerKonfiguration(konfiguration)).isTrue();
        assertThat(geschaeft2.istBerechnetFuerKonfiguration(konfiguration)).isTrue();
    }
}
