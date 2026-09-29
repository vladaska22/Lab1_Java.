import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private static final OrderStatus INITIAL_STATUS = OrderStatus.CREATED;

    private String id;
    private Car car;
    private Priority priority;
    private OrderStatus status;
    private Mechanic mechanic;
    private List<WorkItem> works = new ArrayList<>();

    public Order(String id, Car car, Priority priority) {
        this.id = Validator.requireText(id, "Номер замовлення не може бути порожнім.");
        this.car = Validator.requireNonNull(car, "Замовлення має належати автомобілю.");
        this.priority = Validator.requireNonNull(priority, "Пріоритет замовлення не може бути null.");
        this.status = INITIAL_STATUS;
    }

    public String getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public Priority getPriority() {
        return priority;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public List<WorkItem> getWorks() {
        return Collections.unmodifiableList(works);
    }

    // The methods below change the state and are used only by OrderService.
    void addWork(WorkItem work) {
        works.add(work);
    }

    void assignMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    void changeStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }
}