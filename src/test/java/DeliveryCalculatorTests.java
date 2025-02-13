import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeliveryCalculatorTest {

    @Test
    @Tag("exception")
    @DisplayName("Выброс исключения при хрупком грузе на расстоянии более 30 км")
    @Description("Проверяет, что при попытке доставки хрупкого груза на расстояние более 30 км выбрасывается исключение.")
    void testFragileOver30Km() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> DeliveryCalculator.calculateDeliveryCost(35, false, true, "ordinary")
        );
        assertEquals("Fragile goods cannot be delivered over a distance of more than 30 km", exception.getMessage());
    }

    @Test
    @Tag("calculation")
    @DisplayName("Минимальная стоимость доставки для большого груза")
    @Description("Проверяет, что если рассчитанная стоимость меньше минимальной (400 руб.), то для большого груза устанавливается 550 руб.")
    void testMinDeliveryCostLargeSize() {
        double cost = DeliveryCalculator.calculateDeliveryCost(1, true, true, "ordinary");
        assertEquals(550, cost);
    }

    @Test
    @Tag("calculation")
    @DisplayName("Минимальная стоимость доставки для маленького груза")
    @Description("Проверяет, что если рассчитанная стоимость меньше минимальной (400 руб.), то для маленького груза устанавливается 450 руб.")
    void testMinDeliveryCostSmallSize() {
        double cost = DeliveryCalculator.calculateDeliveryCost(1, false, true, "ordinary");
        assertEquals(450, cost);
    }

    @Test
    @Tag("workload")
    @DisplayName("Стоимость доставки при очень высокой загруженности")
    @Description("Проверяет расчет стоимости для маленького хрупкого груза при загруженности 'very high'.")
    void testSmallSizeVeryHighWorkload() {
        double cost = DeliveryCalculator.calculateDeliveryCost(5, false, true, "very high");
        assertEquals(800, cost);
    }

    @Test
    @Tag("workload")
    @DisplayName("Стоимость доставки при повышенной загруженности")
    @Description("Проверяет расчет стоимости для маленького хрупкого груза при загруженности 'increased'.")
    void testSmallSizeIncreasedOverload() {
        double cost = DeliveryCalculator.calculateDeliveryCost(30, false, true, "increased");
        assertEquals(720, cost);
    }

    @Test
    @Tag("calculation")
    @DisplayName("Минимальная стоимость доставки без надбавок")
    @Description("Проверяет, что если груз не является крупным или хрупким, и коэффициент загруженности обычный, то стоимость не опускается ниже 400 руб.")
    void testMinimalCost() {
        double cost = DeliveryCalculator.calculateDeliveryCost(1, false, false, "ordinary");
        assertEquals(400, cost);
    }

    @Test
    @Tag("calculation")
    @DisplayName("Стоимость доставки на расстояние более 30 км без хрупкости")
    @Description("Проверяет стоимость доставки при расстоянии 31 км без хрупкости и загруженности 'high'.")
    void testVeryLongDistance() {
        double cost = DeliveryCalculator.calculateDeliveryCost(31, false, false, "high");
        assertEquals(560, cost);
    }
}
