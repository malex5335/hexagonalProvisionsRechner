package org.example.provisionsarten;

import org.example.BerechnungInputTestAdapter;
import org.example.BerechnungOutputTestAdapter;
import org.example.TestProdukt;
import org.example.TestVermittler;
import org.example.provisionsberechnung.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    public final List<Produkt> produkte = new ArrayList<>();
    public final List<Vermittler> vermittler_ = new ArrayList<>();
    public final List<Geschaeft> geschaefte = new ArrayList<>();
    public final List<Konfiguration> konfigurationen = new ArrayList<>();
    public BerechnungOutputTestAdapter outputAdapter;
    public BerechnungInputTestAdapter inputAdapter;
    public Berechnung berechnung;
    public TestVermittler vermittler;
    public TestProdukt produkt;
    public BigDecimal geldProGeschaeft;
    public StandardProvision konfiguration;

    @BeforeEach
    void setUp() {
        outputAdapter = new BerechnungOutputTestAdapter();
        inputAdapter = new BerechnungInputTestAdapter()
                .mitVermittler(vermittler_)
                .mitProukten(produkte)
                .mitKonfigurationen(konfigurationen)
                .mitGeschaeften(geschaefte);
        berechnung = new Berechnung(inputAdapter, outputAdapter);
        vermittler = defaultVermittler();
        vermittler_.add(vermittler);
        produkt = defaultProdukt();
        produkte.add(produkt);
        geldProGeschaeft = new BigDecimal(10);
        konfiguration = new StandardProvision(produkt, vermittler, geldProGeschaeft);
        konfigurationen.add(konfiguration);
    }

    @AfterEach
    void tearDown() {
        produkte.clear();
        vermittler_.clear();
        geschaefte.clear();
        konfigurationen.clear();
        outputAdapter = null;
        inputAdapter = null;
        berechnung = null;
        vermittler = null;
        produkt = null;
        geldProGeschaeft = null;
        konfiguration = null;
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 10, 100, 5000})
    public void produktSpezifisch_berechnet(int geschaefteAnzahl) throws InterruptedException {
        // given
        geschaefte.addAll(erstelleGeschaefte(produkt, null, geschaefteAnzahl));

        // when
        // TODO: why do I need to wait 1ms so that java can update all references? || this was here before parameter test!
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe)
                .isEqualByComparingTo(geldProGeschaeft.multiply(new BigDecimal(geschaefteAnzahl)));
        assertTrue(geschaefte.stream()
                .allMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void produktSpezifisch_konfigurationMitAnderemProdukt_nichtBerechnet() {
        // given
        var anderesProdukt = defaultProdukt()
                .mitProduktName("ein anderes Produkt");
        produkte.add(anderesProdukt);
        konfiguration.mitProdukt(anderesProdukt);
        geschaefte.addAll(erstelleGeschaefte(produkt, null, 1));

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
        var anderesProdukt = defaultProdukt()
                .mitProduktName("ein anderes Produkt");
        produkte.add(anderesProdukt);
        geschaefte.addAll(erstelleGeschaefte(anderesProdukt, null, 1));

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
        geschaefte.addAll(erstelleGeschaefte(produkt, null, 1, Geschaeft.Status.LEAD));

        // when
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 10, 100, 5000})
    public void vermittlerSpezifisch_berechnet(int geschaeftAnzahl) throws InterruptedException {
        // given
        konfiguration.mitVermitter(vermittler);
        geschaefte.addAll(erstelleGeschaefte(produkt, vermittler, geschaeftAnzahl));

        // when
        // TODO: why do I need to wait 1ms so that java can update all references? || this was here before parameter test!
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneVermittlerSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe)
                .isEqualByComparingTo(geldProGeschaeft.multiply(new BigDecimal(geschaeftAnzahl)));
        assertTrue(geschaefte.stream()
                .allMatch(geschaeft -> geschaeft.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void vermittlerSpezifisch_konfigurationMitAnderemProdukt_nichtBerechnet() {
        // given
        var anderesProdukt = defaultProdukt()
                .mitProduktName("ein anderes Produkt");
        produkte.add(anderesProdukt);
        konfiguration.mitVermitter(vermittler);
        konfiguration.mitProdukt(anderesProdukt);
        geschaefte.addAll(erstelleGeschaefte(produkt, vermittler, 1));

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
        var anderesProdukt = defaultProdukt()
                .mitProduktName("ein anderes Produkt");
        produkte.add(anderesProdukt);
        konfiguration.mitVermitter(vermittler);
        geschaefte.addAll(erstelleGeschaefte(anderesProdukt, vermittler, 1));

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
        var andererVermittler = defaultVermittler()
                .mitVermittlerNummer("eine andere Vermittlernummer");
        konfiguration.mitVermitter(andererVermittler);
        vermittler_.add(andererVermittler);
        geschaefte.addAll(erstelleGeschaefte(produkt, vermittler, 1));

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
        var andererVermittler = defaultVermittler()
                .mitVermittlerNummer("eine andere Vermittlernummer");
        vermittler_.add(andererVermittler);
        konfiguration.mitVermitter(vermittler);
        geschaefte.addAll(erstelleGeschaefte(produkt, andererVermittler, 1));

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
        konfiguration.mitVermitter(vermittler);
        geschaefte.addAll(erstelleGeschaefte(produkt, vermittler, 1, Geschaeft.Status.LEAD));

        // when
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .noneMatch(g -> g.istBerechnetFuerKonfiguration(konfiguration)));
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
