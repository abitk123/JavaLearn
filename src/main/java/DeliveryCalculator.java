public class DeliveryCalculator {

    public static double calculateDeliveryCost(double distance, boolean isLargeSize, boolean isFragile, String workload) {
        if (distance < 0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }
        if (workload == null || workload.trim().isEmpty()) {
            throw new IllegalArgumentException("Workload cannot be null or empty");
        }

        if (!workload.matches("(?i)ordinary|increased|high|very high")) {
            throw new IllegalArgumentException("Invalid workload value: " + workload);
        }

        if (isFragile && distance > 30) {
            throw new IllegalArgumentException("Fragile goods cannot be delivered over a distance of more than 30 km");
        }

        double cost = 0;

        if (distance > 30) {
            cost += 300;
        } else if (distance > 10) {
            cost += 200;
        } else if (distance > 2) {
            cost += 100;
        } else {
            cost += 50;
        }

        cost += isLargeSize ? 200 : 100;

        if (isFragile) {
            cost += 300;
        }

        double coefficient = switch (workload.toLowerCase()) {
            case "very high" -> 1.6;
            case "high" -> 1.4;
            case "increased" -> 1.2;
            default -> 1.0;
        };

        cost *= coefficient;

        return Math.max(cost, 400);
    }
}
