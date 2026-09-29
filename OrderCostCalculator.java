import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

public class OrderCostCalculator {

    private static final BigDecimal URGENT_LABOR_MULTIPLIER = new BigDecimal("1.20");
    private static final int MONEY_SCALE = 2;

  
    public BigDecimal calculate(Order order) {
        BigDecimal parts = sum(order.getWorks(), WorkItem::getPartsCost);
        BigDecimal labor = sum(order.getWorks(), WorkItem::getLaborCost);

        if (order.getPriority() == Priority.URGENT) {
            labor = labor.multiply(URGENT_LABOR_MULTIPLIER);
        }
        return parts.add(labor).setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal sum(List<WorkItem> works, Function<WorkItem, BigDecimal> cost) {
        return works.stream().map(cost).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}