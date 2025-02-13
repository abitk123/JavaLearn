
import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryCalculatorTest {

    @ParameterizedTest
    @Tag("exception")
    @DisplayName("Exception throw for fragile cargo at a distance of more than 30 km")
    @Description("Checks that if the cargo is fragile and the distance is more than 30 km, an exception is thrown.")
    @CsvSource({
            "35, false, true, ordinary"
    })
    void testFragileOver30Km(double distance, boolean isLarge, boolean isFragile, String workload) {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> DeliveryCalculator.calculateDeliveryCost(distance, isLarge, isFragile, workload)
        );
        assertEquals("Fragile goods cannot be delivered over a distance of more than 30 km", exception.getMessage());
    }

    @ParameterizedTest
    @Tag("calculation")
    @DisplayName("Minimum shipping cost")
    @Description("Checks that if the calculated cost is less than the minimum, the minimum price is set.")
    @CsvSource({
            "1, false, false, ordinary, 400",
            "5, false, false, ordinary, 400",
            "30, false, false, ordinary, 400"
    })
    void testMinDeliveryCost(double distance, boolean isLarge, boolean isFragile, String workload, double expected) {
        double cost = DeliveryCalculator.calculateDeliveryCost(distance, isLarge, isFragile, workload);
        assertEquals(expected, cost);
    }

    @ParameterizedTest
    @Tag("workload")
    @DisplayName("Check the cost")
    @Description("Checks the cost calculation for small fragile cargo with different loading conditions.")
    @CsvSource({
            "5, false, true, very high, 800",
            "30, false, true, increased, 720",
            "1, true, true, ordinary, 550",
            "1, false, true, ordinary, 450",
            "31, false, false, very high, 640",
            "31, true, false, high, 700",

    })
    void testSmallSizeWithDifferentWorkloads(double distance, boolean isLarge, boolean isFragile, String workload, double expected) {
        double cost = DeliveryCalculator.calculateDeliveryCost(distance, isLarge, isFragile, workload);
        assertEquals(expected, cost);
    }

    @ParameterizedTest
    @Tag("calculation")
    @DisplayName("Cost calculation for long distances without fragility")
    @Description("Checks delivery costs for distances over 30 km without fragility and with different levels of congestion.")
    @CsvSource({
            "31, false, false, high, 560"
    })
    void testVeryLongDistance(double distance, boolean isLarge, boolean isFragile, String workload, double expected) {
        double cost = DeliveryCalculator.calculateDeliveryCost(distance, isLarge, isFragile, workload);
        assertEquals(expected, cost);
    }

    @ParameterizedTest
    @Tag("negative")
    @DisplayName("Errors handling")
    @Description("Proves that the method correctly handles uncorrected input data.")
    @CsvSource({
            "-1, false, false, ordinary",
            "10, false, false, ''",
            "10, false, false, 'invalid'"
    })
    void testHandleNegativeValues(double distance, boolean isLarge, boolean isFragile, String workload) {
        assertThrows(IllegalArgumentException.class, () ->
                DeliveryCalculator.calculateDeliveryCost(distance, isLarge, isFragile, workload)
        );
    }

}
