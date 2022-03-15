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
        var testInput = new TestBerechnungInputPort();
        var testOutput = new TestBerechnungOutputPort();
        var berechnung = new Berechnung(testInput, testOutput);

        // when
        berechnung.berechneGeschäfte();

        // then

    }
}
