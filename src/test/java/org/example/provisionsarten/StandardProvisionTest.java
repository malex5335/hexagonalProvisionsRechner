package org.example.provisionsarten;

import org.example.BerechnungInputTestAdapter;
import org.example.BerechnungOutputTestAdapter;
import org.example.TestProdukt;
import org.example.TestVermittler;
import org.example.provisionsberechnung.*;
import org.jetbrains.annotations.NotNull;
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

public class StandardProvisionTest {

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
    public StandardProvision provision;

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
        provision = new StandardProvision()
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
        vermittler = null;
        produkt = null;
        geldProGeschaeft = null;
        provision = null;
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 10, 100, 5000})
    public void produktSpezifisch_berechnet(int geschaefteAnzahl) throws InterruptedException {
        // given
        geschaefte_.addAll(erstelleGeschaefte(produkt, vermittler, geschaefteAnzahl));

        // when
        // TODO: why do I need to wait 1ms so that java can update all references? || this was here before parameter test!
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneProduktSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler))
                .isEqualByComparingTo(geldProGeschaeft.multiply(new BigDecimal(geschaefteAnzahl)));
        assertTrue(geschaefte_.stream()
                .allMatch(geschaeft -> geschaeft.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void produktSpezifisch_provisionMitAnderemProdukt_nichtBerechnet() {
        // given
        var anderesProdukt = defaultProdukt()
                .mitProduktName("ein anderes Produkt");
        produkte_.add(anderesProdukt);
        provision.mitProdukt(anderesProdukt);
        geschaefte_.addAll(erstelleGeschaefte(produkt, vermittler, 1));

        // when
        berechnung.berechneProduktSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void produktSpezifisch_geschaeftMitAnderemProdukt_nichtBerechnet() {
        // given
        var anderesProdukt = defaultProdukt()
                .mitProduktName("ein anderes Produkt");
        produkte_.add(anderesProdukt);
        geschaefte_.addAll(erstelleGeschaefte(anderesProdukt, vermittler, 1));

        // when
        berechnung.berechneProduktSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void produktSpezifisch_geschaeftKeinSale_nichtBerechnet() {
        // given
        geschaefte_.addAll(erstelleGeschaefte(produkt, vermittler, 1, Geschaeft.Status.LEAD));

        // when
        berechnung.berechneProduktSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerProvision(provision)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 10, 100, 5000})
    public void vermittlerSpezifisch_berechnet(int geschaeftAnzahl) throws InterruptedException {
        // given
        provision.mitVermittler(vermittler);
        geschaefte_.addAll(erstelleGeschaefte(produkt, vermittler, geschaeftAnzahl));

        // when
        // TODO: why do I need to wait 1ms so that java can update all references? || this was here before parameter test!
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneVermittlerSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler))
                .isEqualByComparingTo(geldProGeschaeft.multiply(new BigDecimal(geschaeftAnzahl)));
        assertTrue(geschaefte_.stream()
                .allMatch(geschaeft -> geschaeft.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void vermittlerSpezifisch_provisionMitAnderemProdukt_nichtBerechnet() {
        // given
        var anderesProdukt = defaultProdukt()
                .mitProduktName("ein anderes Produkt");
        produkte_.add(anderesProdukt);
        provision.mitVermittler(vermittler)
                .mitProdukt(anderesProdukt);
        geschaefte_.addAll(erstelleGeschaefte(produkt, vermittler, 1));

        // when
        berechnung.berechneVermittlerSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void vermittlerSpezifisch_geschaeftMitAnderemProdukt_nichtBerechnet() {
        // given
        var anderesProdukt = defaultProdukt()
                .mitProduktName("ein anderes Produkt");
        produkte_.add(anderesProdukt);
        provision.mitVermittler(vermittler);
        geschaefte_.addAll(erstelleGeschaefte(anderesProdukt, vermittler, 1));

        // when
        berechnung.berechneVermittlerSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void vermittlerSpezifisch_provisionMitAnderemVermittler_nichtBerechnet() {
        // given
        var andererVermittler = defaultVermittler()
                .mitVermittlerNummer("eine andere Vermittlernummer");
        provision.mitVermittler(andererVermittler);
        vermittler_.add(andererVermittler);
        geschaefte_.addAll(erstelleGeschaefte(produkt, vermittler, 1));

        // when
        berechnung.berechneVermittlerSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void vermittlerSpezifisch_geschaeftMitAnderemVermittler_nichtBerechnet() {
        // given
        var andererVermittler = defaultVermittler()
                .mitVermittlerNummer("eine andere Vermittlernummer");
        vermittler_.add(andererVermittler);
        provision.mitVermittler(vermittler);
        geschaefte_.addAll(erstelleGeschaefte(produkt, andererVermittler, 1));

        // when
        berechnung.berechneVermittlerSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(geschaeft -> geschaeft.istBerechnetFuerProvision(provision)));
    }

    @Test
    public void vermittlerSpezifisch_geschaeftKeinSale_nichtBerechnet() {
        // given
        provision.mitVermittler(vermittler);
        geschaefte_.addAll(erstelleGeschaefte(produkt, vermittler, 1, Geschaeft.Status.LEAD));

        // when
        berechnung.berechneProduktSpezifischeProvisionen();

        // then
        assertThat(outputAdapter.geldFuerVermittler(vermittler)).isEqualByComparingTo(BigDecimal.ZERO);
        assertTrue(geschaefte_.stream()
                .noneMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    private List<Geschaeft> erstelleGeschaefte(Produkt produkt, @NotNull Vermittler vermittler, int count) {
        return erstelleGeschaefte(produkt, vermittler, count, null);
    }

    private List<Geschaeft> erstelleGeschaefte(Produkt produkt, @NotNull Vermittler vermittler, int count, Geschaeft.Status status) {
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
