import org.example.Berechnung;
import org.example.Geschäft;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BerechnungTest {
    @BeforeAll
    static void setUp() {
    }

    @AfterAll
    static void tearDown() {
    }

    @Test
    public void berechnung_erfolgreich() {
        // given
        var geld = new BigDecimal(10);
        var vermittler = new TestVermittler();
        var produkt = new TestProdukt();
        var geschäft = new TestGeschäft()
                .mitAnlieferDatum(LocalDateTime.now())
                .mitStatus(Geschäft.Status.SALE)
                .fürProdukt(produkt)
                .fürVermittler(vermittler);
        var konfiguration = new TestKonfiguration();

        // when
        var inputAdapter = new BerechnungInputTestAdapter(geschäft, konfiguration);
        var outputAdapter = new BerechnungOutputTestAdapter(geschäft);
        var berechnung = new Berechnung(inputAdapter, outputAdapter);
        berechnung.berechneGeschäfte();

        // then
        outputAdapter.mitGeldFürGeschäft(geschäft, geld);
        // output adapter sollte zurückgeben, dass das Geschäft berechnet wurde
        // der berechnete Betrag sollte ebenfalls stimmen
        // ggf. weitere Tests
    }
}
