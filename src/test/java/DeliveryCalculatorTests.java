
import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryCalculatorTest {

    @ParameterizedTest
    @Tag("exception")
    @DisplayName("Выброс исключения при хрупком грузе на расстоянии более 30 км")
    @Description("Проверяет, что если груз хрупкий и расстояние больше 30 км, выбрасывается исключение.")
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
    @DisplayName("Минимальная стоимость доставки")
    @Description("Проверяет, что если рассчитанная стоимость меньше минимальной, устанавливается минимальная цена.")
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
    @DisplayName("Проверка стоимости при разной загруженности")
    @Description("Проверяет расчет стоимости для маленького хрупкого груза при разной загруженности.")
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
    @DisplayName("Расчет стоимости для больших расстояний без хрупкости")
    @Description("Проверяет стоимость доставки при расстоянии более 30 км без хрупкости и с разными уровнями загруженности.")
    @CsvSource({
            "31, false, false, high, 560"
    })
    void testVeryLongDistance(double distance, boolean isLarge, boolean isFragile, String workload, double expected) {
        double cost = DeliveryCalculator.calculateDeliveryCost(distance, isLarge, isFragile, workload);
        assertEquals(expected, cost);
    }

    @ParameterizedTest
    @Tag("negative")
    @DisplayName("Тест: обработка некорректных значений")
    @Description("Проверяет, что метод корректно обрабатывает некорректные входные данные и выбрасывает исключение.")
    @CsvSource({
            "-1, false, false, ordinary",  // Отрицательное расстояние
            "10, false, false, ''",        // Пустая строка workload
            "10, false, false, 'invalid'"  // Неверное значение workload
    })
    void testHandleNegativeValues(double distance, boolean isLarge, boolean isFragile, String workload) {
        assertThrows(IllegalArgumentException.class, () ->
                DeliveryCalculator.calculateDeliveryCost(distance, isLarge, isFragile, workload)
        );
    }

}
