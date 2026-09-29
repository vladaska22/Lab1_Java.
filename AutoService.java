import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores and looks up all registered objects of the car service.
 */
public class AutoService {

    private List<Client> clients = new ArrayList<>();
    private Map<Long, Mechanic> mechanicsById = new HashMap<>();
    private Map<String, Car> carsByVin = new HashMap<>();
    private Map<String, Order> ordersById = new LinkedHashMap<>();

    public void registerClient(Client client) {
        clients.add(Validator.requireNonNull(client, "Клієнт не може бути null."));
    }

    public void registerMechanic(Mechanic mechanic) {
        Validator.requireNonNull(mechanic, "Механік не може бути null.");
        if (mechanicsById.putIfAbsent(mechanic.getId(), mechanic) != null) {
            throw new DomainException("Механік з ідентифікатором " + mechanic.getId() + " вже зареєстрований.");
        }
    }

    public void registerCar(Car car) {
        Validator.requireNonNull(car, "Автомобіль не може бути null.");
        if (!clients.contains(car.getOwner())) {
            throw new DomainException("Власник автомобіля не зареєстрований в автосервісі.");
        }
        // One map operation checks uniqueness and stores the car.
        if (carsByVin.putIfAbsent(toKey(car.getVin()), car) != null) {
            throw new DomainException("Автомобіль з VIN " + car.getVin() + " вже зареєстрований.");
        }
    }

    public boolean isCarRegistered(Car car) {
        return car != null && carsByVin.get(toKey(car.getVin())) == car;
    }

    public void addOrder(Order order) {
        ordersById.put(order.getId(), order);
    }

    public boolean isMechanicBusy(Mechanic mechanic, Order currentOrder) {
        return ordersById.values().stream()
                .filter(order -> order != currentOrder)
                .filter(order -> !order.getStatus().isClosed())
                .anyMatch(order -> hasMechanic(order, mechanic));
    }

    private boolean hasMechanic(Order order, Mechanic mechanic) {
        return order.getMechanic() != null && order.getMechanic().getId() == mechanic.getId();
    }

    // VIN is compared case-insensitively.
    private String toKey(String vin) {
        return vin.toUpperCase();
    }
}