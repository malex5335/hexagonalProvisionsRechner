package org.example.provisionsarten;

import org.example.BerechnungInputTestAdapter;
import org.example.BerechnungOutputTestAdapter;
import org.example.TestProdukt;
import org.example.TestVermittler;
import org.example.provisionsberechnung.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.example.TestGeschaeft.defaultGeschaeft;

public class StandardBerechnungTest {

    public static final List<Produkt> produkte = new ArrayList<>();
    public static final List<Vermittler> vermittler_ = new ArrayList<>();
    public static final List<Geschaeft> geschaefte = new ArrayList<>();
    public static final List<Konfiguration> konfigurationen = new ArrayList<>();
    public static BerechnungOutputTestAdapter outputAdapter;
    public static BerechnungInputTestAdapter inputAdapter;
    public static Berechnung berechnung;

    @BeforeEach
    void setUp() {
        outputAdapter = new BerechnungOutputTestAdapter();
        inputAdapter = new BerechnungInputTestAdapter()
                .mitVermittler(vermittler_)
                .mitProukten(produkte)
                .mitKonfigurationen(konfigurationen)
                .mitGeschaeften(geschaefte);
        berechnung = new Berechnung(inputAdapter, outputAdapter);
    }

    @AfterEach
    void tearDown() {
        outputAdapter = null;
        inputAdapter = null;
        berechnung = null;
        produkte.clear();
        vermittler_.clear();
        geschaefte.clear();
        konfigurationen.clear();
    }

    @Test
    public void produktSpezifisch_erfolgreich() throws InterruptedException {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = new TestProdukt();
        var konfiguration = new StandardProvision(produkt, null, geldProGeschaeft);
        produkte.add(produkt);
        geschaefte.addAll(erstelleGeschaefte(produkt, null, 1));
        konfigurationen.add(konfiguration);

        // when
        TimeUnit.MILLISECONDS.sleep(1); // TODO: why do I need to wait 1ms so that java can update all references? || and why only in this method?
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(geldProGeschaeft);
        assertTrue(geschaefte.stream()
                .allMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void produktSpezifisch_100Geschaefte_erfolgreich() throws InterruptedException {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = new TestProdukt();
        var konfiguration = new StandardProvision(produkt, null, geldProGeschaeft);
        produkte.add(produkt);
        geschaefte.addAll(erstelleGeschaefte(produkt, null, 100));
        konfigurationen.add(konfiguration);

        // when
        TimeUnit.MILLISECONDS.sleep(1); // TODO: why do I need to wait 1ms so that java can update all references? || and why only in this method?
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(new BigDecimal(1000));
        assertTrue(geschaefte.stream()
                .allMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void produktSpezifisch_konfigurationMitAnderemProdukt_nichtBerechnet() {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = new TestProdukt()
                .mitProduktName("ein Produkt");
        var anderesProdukt = new TestProdukt()
                .mitProduktName("ein anderes Produkt");
        var konfiguration = new StandardProvision(anderesProdukt, null, geldProGeschaeft);
        produkte.add(produkt);
        produkte.add(anderesProdukt);
        geschaefte.addAll(erstelleGeschaefte(produkt, null, 1));
        konfigurationen.add(konfiguration);

        // when
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void produktSpezifisch_geschaeftMitAnderemProdukt_nichtBerechnet() {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = new TestProdukt()
                .mitProduktName("ein Produkt");
        var anderesProdukt = new TestProdukt()
                .mitProduktName("ein anderes Produkt");
        var konfiguration = new StandardProvision(produkt, null, geldProGeschaeft);
        produkte.add(produkt);
        produkte.add(anderesProdukt);
        geschaefte.addAll(erstelleGeschaefte(anderesProdukt, null, 1));
        konfigurationen.add(konfiguration);

        // when
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void vermittlerSpezifisch_erfolgreich() throws InterruptedException {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = new TestProdukt();
        var vermittler = new TestVermittler();
        var konfiguration = new StandardProvision(produkt, vermittler, geldProGeschaeft);
        produkte.add(produkt);
        vermittler_.add(vermittler);
        geschaefte.addAll(erstelleGeschaefte(produkt, vermittler, 1));
        konfigurationen.add(konfiguration);

        // when
        TimeUnit.MILLISECONDS.sleep(1); // TODO: why do I need to wait 1ms so that java can update all references? || and why only in this method?
        berechnung.berechneVermittlerSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(geldProGeschaeft);
        assertTrue(geschaefte.stream()
                .allMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void vermittlerSpezifisch_100Geschaefte_erfolgreich() {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = new TestProdukt();
        var vermittler = new TestVermittler();
        var konfiguration = new StandardProvision(produkt, vermittler, geldProGeschaeft);
        produkte.add(produkt);
        vermittler_.add(vermittler);
        geschaefte.addAll(erstelleGeschaefte(produkt, vermittler, 100));
        konfigurationen.add(konfiguration);

        // when
        berechnung.berechneVermittlerSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(new BigDecimal(1000));
        assertTrue(geschaefte.stream()
                .allMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void vermittlerSpezifisch_konfigurationMitAnderemProdukt_nichtBerechnet() {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = new TestProdukt()
                .mitProduktName("ein Produkt");
        var anderesProdukt = new TestProdukt()
                .mitProduktName("ein anderes Produkt");
        var vermittler = new TestVermittler();
        var konfiguration = new StandardProvision(anderesProdukt, vermittler, geldProGeschaeft);
        produkte.add(produkt);
        produkte.add(anderesProdukt);
        vermittler_.add(vermittler);
        geschaefte.addAll(erstelleGeschaefte(produkt, vermittler, 1));
        konfigurationen.add(konfiguration);

        // when
        berechnung.berechneVermittlerSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void vermittlerSpezifisch_geschaeftMitAnderemProdukt_nichtBerechnet() {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = new TestProdukt()
                .mitProduktName("ein Produkt");
        var anderesProdukt = new TestProdukt()
                .mitProduktName("ein anderes Produkt");
        var vermittler = new TestVermittler();
        var konfiguration = new StandardProvision(produkt, vermittler, geldProGeschaeft);
        produkte.add(produkt);
        produkte.add(anderesProdukt);
        vermittler_.add(vermittler);
        geschaefte.addAll(erstelleGeschaefte(anderesProdukt, vermittler, 1));
        konfigurationen.add(konfiguration);

        // when
        berechnung.berechneVermittlerSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    private List<Geschaeft> erstelleGeschaefte(Produkt produkt, Vermittler vermittler, int count) {
        var geschaefte = new ArrayList<Geschaeft>();
        while(--count >= 0) {
            geschaefte.add(
                    defaultGeschaeft()
                    .mitProdukt(produkt)
                    .mitVermittler(vermittler)
            );
        }
        return geschaefte;
    }
}
