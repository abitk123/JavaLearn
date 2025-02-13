import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FirstUnitTest {
    @Test
    void sumNumbersTest(){
        int a = 3;
        int b = 2;
        int expected = 5;
        int actualSum = a + b;

        assertEquals(expected, actualSum);
    }
}
