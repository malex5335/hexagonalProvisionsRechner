package org.example.provisionsarten;

import org.example.BerechnungInputTestAdapter;
import org.example.BerechnungOutputTestAdapter;
import org.example.TestProdukt;
import org.example.TestVermittler;
import org.example.provisionsberechnung.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.TestGeschaeft.defaultGeschaeft;
import static org.example.TestProdukt.defaultProdukt;
import static org.example.TestVermittler.defaultVermittler;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VolumenStandardProvisionTest {
    public final List<Produkt> produkte_ = new ArrayList<>();
    public final List<Vermittler> vermittler_ = new ArrayList<>();
    public final List<Geschaeft> geschaefte_ = new ArrayList<>();
    public final List<Provision> provisionen_ = new ArrayList<>();
    public BerechnungOutputTestAdapter outputAdapter;
    public BerechnungInputTestAdapter inputAdapter;
    public Berechnung berechnung;
    public TestVermittler vermittler;
    public TestProdukt produkt;
    public StandardVolumenProvision provision;

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
        provision = new StandardVolumenProvision()
                .mitProdukt(produkt);
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
        provision = null;
    }

    private static Stream<Arguments> berechnet_params() {
        return Stream.of(
                Arguments.of(new BigDecimal(1000), new BigDecimal(10), new BigDecimal(100)),
                Arguments.of(new BigDecimal(1000), new BigDecimal(0), BigDecimal.ZERO),
                Arguments.of(new BigDecimal(1000), new BigDecimal(25), new BigDecimal(250)),
                Arguments.of(new BigDecimal(1000), new BigDecimal("1.5"), new BigDecimal(15)),
                Arguments.of(new BigDecimal(100), new BigDecimal("1.5"), new BigDecimal("1.5")),
                Arguments.of(new BigDecimal(56789), new BigDecimal(22), new BigDecimal("12493.58")),
                Arguments.of(new BigDecimal(56789), new BigDecimal("12.3"), new BigDecimal("6985.05"))
        );
    }

    @ParameterizedTest
    @MethodSource("berechnet_params")
    public void produktSpezifisch_berechnet(BigDecimal volumen, BigDecimal prozent, BigDecimal ergebnis) throws InterruptedException {
        // given
        var feld = UUID.randomUUID().toString();
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVolumenBetrag(feld, volumen);
        geschaefte_.add(geschaeft);
        provision.mitVolumenFeld(feld)
                .mitProzentProGeschaeft(prozent);

        // when
        // TODO: why do I need to wait 1ms so that java can update all references? || this was here before parameter test!
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneProduktSpezifischeProvisioenn();

        //then
        assertThat(outputAdapter.summe).isEqualByComparingTo(ergebnis);
        assertTrue(geschaefte_.stream()
                .allMatch(g -> g.istBerechnetFuerProvision(provision)));
    }

    @ParameterizedTest
    @MethodSource("berechnet_params")
    public void vermittlerSpezifisch_berechnet(BigDecimal volumen, BigDecimal prozent, BigDecimal ergebnis) throws InterruptedException {
        // given
        var feld = UUID.randomUUID().toString();
        var geschaeft = defaultGeschaeft()
                .mitProdukt(produkt)
                .mitVermittler(vermittler)
                .mitVolumenBetrag(feld, volumen);
        geschaefte_.add(geschaeft);
        provision.mitVermittler(vermittler)
                .mitVolumenFeld(feld)
                .mitProzentProGeschaeft(prozent);

        // when
        // TODO: why do I need to wait 1ms so that java can update all references? || this was here before parameter test!
        TimeUnit.MILLISECONDS.sleep(1);
        berechnung.berechneProduktSpezifischeProvisioenn();

        //then
        assertThat(outputAdapter.summe).isEqualByComparingTo(ergebnis);
        assertTrue(geschaefte_.stream()
                .allMatch(g -> g.istBerechnetFuerProvision(provision)));

    }
}
