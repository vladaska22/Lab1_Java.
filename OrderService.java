import java.math.BigDecimal;

/**
 * Business rules and life cycle of orders.
 */
public class OrderService {

    private AutoService autoService;
    private OrderIdGenerator idGenerator = new OrderIdGenerator();
    private OrderCostCalculator costCalculator = new OrderCostCalculator();

    public OrderService(AutoService autoService) {
        this.autoService = Validator.requireNonNull(autoService, "Автосервіс не може бути null.");
    }

    public Order createOrder(Car car, Priority priority) {
        if (!autoService.isCarRegistered(car)) {
            throw new DomainException("Замовлення можна створити лише для зареєстрованого автомобіля.");
        }
        Order order = new Order(idGenerator.next(), car, priority);
        autoService.addOrder(order);
        return order;
    }

    public void addWork(Order order, WorkItem work) {
        checkNotClosed(order);
        Validator.requireNonNull(work, "Робота не може бути null.");
        order.addWork(work);
    }

    public void diagnose(Order order) {
        moveTo(order, OrderStatus.DIAGNOSED);
    }

    public void approve(Order order) {
        checkTransition(order, OrderStatus.APPROVED);
        if (order.getWorks().isEmpty()) {
            throw new DomainException("Не можна погодити замовлення без робіт.");
        }
        order.changeStatus(OrderStatus.APPROVED);
    }

    public void assignMechanic(Order order, Mechanic mechanic) {
        checkNotClosed(order);
        Validator.requireNonNull(mechanic, "Механік не може бути null.");
        if (autoService.isMechanicBusy(mechanic, order)) {
            throw new DomainException("Механік " + mechanic.getName() + " вже зайнятий іншим активним замовленням.");
        }
        order.assignMechanic(mechanic);
    }

    public void start(Order order) {
        checkTransition(order, OrderStatus.IN_PROGRESS);
        if (order.getMechanic() == null) {
            throw new DomainException("Неможливо розпочати виконання без призначеного механіка.");
        }
        order.changeStatus(OrderStatus.IN_PROGRESS);
    }

    public void completeWork(Order order, WorkItem work) {
        checkNotClosed(order);
        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new DomainException("Роботи можна виконувати лише в статусі IN_PROGRESS.");
        }
        if (!order.getWorks().contains(work)) {
            throw new DomainException("Ця робота не належить замовленню " + order.getId() + ".");
        }
        work.complete();
    }

    public void complete(Order order) {
        checkTransition(order, OrderStatus.COMPLETED);
        boolean allWorksCompleted = order.getWorks().stream().allMatch(WorkItem::isCompleted);
        if (!allWorksCompleted) {
            throw new DomainException("Не всі роботи виконані.");
        }
        order.changeStatus(OrderStatus.COMPLETED);
    }

    public void cancel(Order order) {
        moveTo(order, OrderStatus.CANCELLED);
    }

    public BigDecimal calculateTotalCost(Order order) {
        return costCalculator.calculate(order);
    }

    private void moveTo(Order order, OrderStatus newStatus) {
        checkTransition(order, newStatus);
        order.changeStatus(newStatus);
    }

    private void checkTransition(Order order, OrderStatus newStatus) {
        checkNotClosed(order);
        if (!order.getStatus().canMoveTo(newStatus)) {
            throw new DomainException("Перехід зі статусу " + order.getStatus() + " у " + newStatus + " заборонений.");
        }
    }

    private void checkNotClosed(Order order) {
        Validator.requireNonNull(order, "Замовлення не може бути null.");
        if (order.getStatus().isClosed()) {
            throw new DomainException("Завершене або скасоване замовлення не можна змінювати.");
        }
    }
}