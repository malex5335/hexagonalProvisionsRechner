import org.example.Berechnung;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        var inputAdapter = new BerechnungInputTestAdapter();
        var outputAdapter = new BerechnungOutputTestAdapter();
        var berechnung = new Berechnung(inputAdapter, outputAdapter);

        // when
        berechnung.berechneGeschäfte();

        // then
        // output adapter sollte zurückgeben, dass das Geschäft berechnet wurde
        // der berechnete Betrag sollte ebenfalls stimmen
        // ggf. weitere Tests
    }
}
