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
import static org.example.TestProvision.defaultProvision;
import static org.example.TestProdukt.defaultProdukt;
import static org.example.TestVermittler.defaultVermittler;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BerechnungTest {

    public final List<Produkt> produkte_ = new ArrayList<>();
    public final List<Vermittler> vermittler_ = new ArrayList<>();
    public final List<Geschaeft> geschaefte_ = new ArrayList<>();
    public final List<Provision> provisionen_ = new ArrayList<>();
    public BerechnungOutputTestAdapter outputAdapter;
    public BerechnungInputTestAdapter inputAdapter;
    public Berechnung berechnung;
    public TestVermittler vermittler;
    public TestProdukt produkt;
    public BigDecimal geldProGeschaeft;
    public TestProvision provision;

    @BeforeEach
    void setUp() {
        outputAdapter = new BerechnungOutputTestAdapter();
        inputAdapter = new BerechnungInputTestAdapter()
                .mitVermittler(vermittler_)
                .mitProukten(produkte_)
                .mitProvisionen(provisionen_)
                .mitGeschaeften(geschaefte_);
        berechnung = new Berechnung(inputAdapter, outputAdapter);
        vermittler = defaultVermittler();
        vermittler_.add(vermittler);
        produkt = defaultProdukt();
        produkte_.add(produkt);
        geldProGeschaeft = new BigDecimal(10);
        provision = defaultProvision()
                .mitProdukt(produkt)
                .mitGeldProGeschaeft(geldProGeschaeft);
        provisionen_.add(provision);
    }

    @AfterEach
    void tearDown() {
        produkte_.clear();
        vermittler_.clear();
        geschaefte_.clear();
        provisionen_.clear();
        outputAdapter = null;
        inputAdapter = null;
        berechnung = null;
        produkt = null;
        vermittler = null;
        provision = null;
    }

    @Test
    public void vermittlerSpezifisch_vermittlerKenntGeschaefte_mitBetraegen() throws InterruptedException {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        provision.mitVermittler(vermittler);
        geschaefte_.add(geschaeft);

        // when
        // TODO: why do I need to wait 1ms so that java can update all references?
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneVermittlerSpezifischeProvisionen();

        // then
        assertThat(vermittler.zahlungsReport().berechneteGeschaefte()).contains(geschaeft);
        assertThat(vermittler.zahlungsReport().provisionenFuerGeschaeft(geschaeft)).contains(provision);
        assertThat(vermittler.zahlungsReport().betragFuerGeschaeft(geschaeft, provision))
                .isEqualByComparingTo(geldProGeschaeft);
    }

    @Test
    public void produktSpezifisch_vermittlerKenntGeschaefte_mitBetraegen() throws InterruptedException {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        geschaefte_.add(geschaeft);

        // when
        // TODO: why do I need to wait 1ms so that java can update all references?
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneProduktSpezifischeProvisionen();

        // then
    }

    @Test
    public void vermittlerUndProduktProvision_vermittlerBerechnet_produktBerechnet() throws InterruptedException {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        var vermittlerProvision = defaultProvision()
                .mitProdukt(produkt)
                .mitGeldProGeschaeft(geldProGeschaeft)
                .mitVermittler(vermittler);
        geschaefte_.add(geschaeft);
        provisionen_.add(vermittlerProvision);

        // when
        // TODO: why do I need to wait 1ms so that java can update all references?
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneVermittlerSpezifischeProvisionen();
        berechnung.berechneProduktSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(geldProGeschaeft.multiply(new BigDecimal(2)));
        // produkt provision nicht berechnet
        assertTrue(geschaefte_.stream()
                .allMatch(g -> g.istBerechnetFuerProvision(provision)));
        // vermittler provision berechnet
        assertTrue(geschaefte_.stream()
                .allMatch(g -> g.istBerechnetFuerProvision(vermittlerProvision)));
    }

    @Test
    public void keineVermittlerProvision_produktBerechnet() throws InterruptedException {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        geschaefte_.add(geschaeft);

        // when
        // TODO: why do I need to wait 1ms so that java can update all references?
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneVermittlerSpezifischeProvisionen();
        berechnung.berechneProduktSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(geldProGeschaeft);
        assertTrue(geschaefte_.stream()
                .allMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void keineProduktProvision_vermittlerBerechnet() throws InterruptedException {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        provision.mitVermittler(vermittler);
        geschaefte_.add(geschaeft);

        // when
        // TODO: why do I need to wait 1ms so that java can update all references?
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneVermittlerSpezifischeProvisionen();
        berechnung.berechneProduktSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(geldProGeschaeft);
        assertTrue(geschaefte_.stream()
                .allMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void vermittlerSpezifisch_geschaefteVonUntervermittler_erfolgreichBerechnet() throws InterruptedException {
        // given
        var unterVermittler = defaultVermittler()
                .mitHauptVermittler(vermittler);
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(unterVermittler);
        vermittler_.add(unterVermittler);
        geschaefte_.add(geschaeft);
        provision.mitVermittler(vermittler);

        // when
        // TODO: why do I need to wait 1ms so that java can update all references?
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneVermittlerSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(unterVermittler)).isEqualByComparingTo(geldProGeschaeft);
        assertTrue(geschaefte_.stream()
                .allMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void provisionMitAnderemVermittler_nichtBerechnet() {
        // given
        var andererVermittler = defaultVermittler()
                .mitVermittlerNummer("eine andere Vermittlernummer");
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        vermittler_.add(andererVermittler);
        geschaefte_.add(geschaeft);
        provision.mitVermittler(andererVermittler);

        // when
        berechnung.berechneVermittlerSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void geschaeftMitAnderemVermittler_nichtBerechnet() {
        // given
        var andererVermittler = defaultVermittler()
                .mitVermittlerNummer("eine andere Vermittlernummer");
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(andererVermittler);
        vermittler_.add(andererVermittler);
        geschaefte_.add(geschaeft);

        // when
        berechnung.berechneVermittlerSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(andererVermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void vermittlerSpezifisch_geschaeftBerechnet_keineWeitereBerechnung() {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler)
                .mitBerechnetFuer(provision);
        geschaefte_.add(geschaeft);
        provision.mitVermittler(vermittler);

        // when
        berechnung.berechneVermittlerSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .allMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void produktSpezifisch_geschaeftBerechnet_keineWeitereBerechnung() {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler)
                .mitBerechnetFuer(provision);
        geschaefte_.add(geschaeft);

        // when
        berechnung.berechneProduktSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .allMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void vermittlerSpezifisch_erfolgreichBerechnet() throws InterruptedException {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        geschaefte_.add(geschaeft);
        provision.mitVermittler(vermittler);

        // when
        // TODO: why do I need to wait 1ms so that java can update all references?
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneVermittlerSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(geldProGeschaeft);
        assertTrue(geschaefte_.stream()
                .allMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void produktSpezifisch_erfolgreichBerechnet() throws InterruptedException {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        geschaefte_.add(geschaeft);

        // when
        // TODO: why do I need to wait 1ms so that java can update all references?
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneProduktSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(geldProGeschaeft);
        assertTrue(geschaefte_.stream()
                .allMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void vermittlerSpezifisch_produktNichtAktiv_nichtBerechnet() {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        geschaefte_.add(geschaeft);
        provision.mitVermittler(vermittler);
        produkt.mitAktiv(false);

        // when
        berechnung.berechneVermittlerSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void produktSpezifisch_produktNichtAktiv_nichtBerechnet() {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler);
        geschaefte_.add(geschaeft);
        produkt.mitAktiv(false);

        // when
        berechnung.berechneProduktSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5, 15, 30, 60, 119})
    public void vermittlerSpezifisch_geschaeftNichtAltGenug_nichtBerechnet(int tageZurueck) {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler)
                .mitAnlieferDatum(LocalDate.now().atStartOfDay().minusDays(tageZurueck));
        geschaefte_.add(geschaeft);
        provision.mitVermittler(vermittler);

        // when
        berechnung.berechneVermittlerSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5, 15, 30, 60, 119})
    public void produktSpezifisch_geschaeftNichtAltGenug_nichtBerechnet(int tageZurueck) {
        // given
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler)
                .mitAnlieferDatum(LocalDate.now().atStartOfDay().minusDays(tageZurueck));
        geschaefte_.add(geschaeft);

        // when
        berechnung.berechneProduktSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(g -> g.istBerechnetFuerProvision(provision)));

    }
}
