package org.example.provisionsberechnung;

import org.example.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.TestGeschaeft.defaultGeschaeft;
import static org.example.TestKonfiguration.defaultKonfiguration;
import static org.example.TestProdukt.defaultProdukt;
import static org.example.TestVermittler.defaultVermittler;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BerechnungTest {

    public static final List<Produkt> produkte = new ArrayList<>();
    public static final List<Vermittler> vermittler_ = new ArrayList<>();
    public static final List<Geschaeft> geschaefte = new ArrayList<>();
    public static final List<Konfiguration> konfigurationen = new ArrayList<>();
    public static BerechnungOutputTestAdapter outputAdapter;
    public static BerechnungInputTestAdapter inputAdapter;
    public static Berechnung berechnung;
    public static TestVermittler vermittler;
    public static TestProdukt produkt;
    public static BigDecimal geldProGeschaeft;
    public static TestKonfiguration konfiguration;

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
        konfiguration = defaultKonfiguration()
                .mitProdukt(produkt)
                .mitGeldProGeschaeft(geldProGeschaeft);
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
        produkt = null;
        vermittler = null;
        konfiguration = null;
    }

    @Test
    public void vermittlerSpezifisch_geschaeftBerechnet_keineWeitereBerechnung() {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler)
                .mitBerechnetFuer(konfiguration);
        geschaefte.add(geschaeft);
        konfiguration.mitVermittler(vermittler);

        // when
        berechnung.berechneVermittlerSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .allMatch(g -> g.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void produktSpezifisch_geschaeftBerechnet_keineWeitereBerechnung() {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitBerechnetFuer(konfiguration);
        geschaefte.add(geschaeft);

        // when
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .allMatch(g -> g.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void vermittlerSpezifisch_erfolgreichBerechnet() throws InterruptedException {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        geschaefte.add(geschaeft);
        konfiguration.mitVermittler(vermittler);

        // when
        // TODO: why do I need to wait 1ms so that java can update all references?
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneVermittlerSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(geldProGeschaeft);
        assertTrue(geschaefte.stream()
                .allMatch(g -> g.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void produktSpezifisch_erfolgreichBerechnet() throws InterruptedException {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt);
        geschaefte.add(geschaeft);

        // when
        // TODO: why do I need to wait 1ms so that java can update all references?
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(geldProGeschaeft);
        assertTrue(geschaefte.stream()
                .allMatch(g -> g.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void vermittlerSpezifisch_produktNichtAktiv_nichtBerechnet() {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        geschaefte.add(geschaeft);
        konfiguration.mitVermittler(vermittler);
        produkt.mitAktiv(false);

        // when
        berechnung.berechneVermittlerSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .noneMatch(g -> g.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @Test
    public void produktSpezifisch_produktNichtAktiv_nichtBerechnet() {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt);
        geschaefte.add(geschaeft);
        produkt.mitAktiv(false);

        // when
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .noneMatch(g -> g.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5, 15, 30, 60, 119})
    public void vermittlerSpezifisch_geschaeftNichtAltGenug_nichtBerechnet(int tageZurueck) {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler)
                .mitAnlieferDatum(LocalDate.now().atStartOfDay().minusDays(tageZurueck));
        geschaefte.add(geschaeft);
        konfiguration.mitVermittler(vermittler);

        // when
        berechnung.berechneVermittlerSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .noneMatch(g -> g.istBerechnetFuerKonfiguration(konfiguration)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5, 15, 30, 60, 119})
    public void produktSpezifisch_geschaeftNichtAltGenug_nichtBerechnet(int tageZurueck) {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitAnlieferDatum(LocalDate.now().atStartOfDay().minusDays(tageZurueck));
        geschaefte.add(geschaeft);

        // when
        berechnung.berechneProduktSpezifischeKonfigs();

        // then
        assertThat(outputAdapter.summe).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte.stream()
                .noneMatch(g -> g.istBerechnetFuerKonfiguration(konfiguration)));

    }
}
