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
import static org.example.TestProdukt.defaultProdukt;
import static org.example.TestVermittler.defaultVermittler;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.example.TestGeschaeft.defaultGeschaeft;

public class StandardKonfigurationTest {

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
    public void produktSpezifisch_berechnet() throws InterruptedException {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = defaultProdukt();
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
    public void produktSpezifisch_100Geschaefte_berechnet() throws InterruptedException {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = defaultProdukt();
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
        var produkt = defaultProdukt()
                .mitProduktName("ein Produkt");
        var anderesProdukt = defaultProdukt()
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
        var produkt = defaultProdukt()
                .mitProduktName("ein Produkt");
        var anderesProdukt = defaultProdukt()
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
    public void produktSpezifisch_geschaeftKeinSale_nichtBerechnet() {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = defaultProdukt();
        var konfiguration = new StandardProvision(produkt, null, geldProGeschaeft);
        produkte.add(produkt);
        geschaefte.addAll(erstelleGeschaefte(produkt, null, 1, Geschaeft.Status.LEAD));
        konfigurationen.add(konfiguration);

        // when
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void vermittlerSpezifisch_berechnet() throws InterruptedException {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = defaultProdukt();
        var vermittler = defaultVermittler();
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
    public void vermittlerSpezifisch_100Geschaefte_berechnet() {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = defaultProdukt();
        var vermittler = defaultVermittler();
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
        var produkt = defaultProdukt()
                .mitProduktName("ein Produkt");
        var anderesProdukt = defaultProdukt()
                .mitProduktName("ein anderes Produkt");
        var vermittler = defaultVermittler();
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
        var produkt = defaultProdukt()
                .mitProduktName("ein Produkt");
        var anderesProdukt = defaultProdukt()
                .mitProduktName("ein anderes Produkt");
        var vermittler = defaultVermittler();
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

    @Test
    public void vermittlerSpezifisch_konfigurationMitAnderemVermittler_nichtBerechnet() {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = defaultProdukt();
        var vermittler = defaultVermittler()
                .mitVermittlerNummer("eine Vermittlernummer");
        var andererVermittler = defaultVermittler()
                .mitVermittlerNummer("eine andere Vermittlernummer");
        var konfiguration = new StandardProvision(produkt, andererVermittler, geldProGeschaeft);
        produkte.add(produkt);
        produkte.add(produkt);
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
    public void vermittlerSpezifisch_geschaeftMitAnderemVermittler_nichtBerechnet() {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = defaultProdukt();
        var vermittler = defaultVermittler()
                .mitVermittlerNummer("eine Vermittlernummer");
        var andererVermittler = defaultVermittler()
                .mitVermittlerNummer("eine andere Vermittlernummer");
        var konfiguration = new StandardProvision(produkt, vermittler, geldProGeschaeft);
        geschaefte.addAll(erstelleGeschaefte(produkt, andererVermittler, 1));
        produkte.add(produkt);
        vermittler_.add(vermittler);
        konfigurationen.add(konfiguration);

        // when
        berechnung.berechneVermittlerSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void vermittlerSpezifisch_geschaeftKeinSale_nichtBerechnet() {
        // given
        var geldProGeschaeft = new BigDecimal(10);
        var produkt = defaultProdukt();
        var vermittler = defaultVermittler();
        var konfiguration = new StandardProvision(produkt, vermittler, geldProGeschaeft);
        produkte.add(produkt);
        vermittler_.add(vermittler);
        geschaefte.addAll(erstelleGeschaefte(produkt, vermittler, 1, Geschaeft.Status.LEAD));
        konfigurationen.add(konfiguration);

        // when
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    private List<Geschaeft> erstelleGeschaefte(Produkt produkt, Vermittler vermittler, int count) {
        return erstelleGeschaefte(produkt, vermittler, count, null);
    }

    private List<Geschaeft> erstelleGeschaefte(Produkt produkt, Vermittler vermittler, int count, Geschaeft.Status status) {
        var geschaefte = new ArrayList<Geschaeft>();
        while(--count >= 0) {
            var geschaeft = defaultGeschaeft()
                    .mitProdukt(produkt)
                    .mitVermittler(vermittler);
            if(status != null) {
                geschaeft.mitStatus(status);
            }
            geschaefte.add(
                    geschaeft
            );
        }
        return geschaefte;
    }
}
