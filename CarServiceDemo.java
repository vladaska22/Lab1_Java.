import java.math.BigDecimal;

public class CarServiceDemo {

    public static void main(String[] args) {
        AutoService autoService = new AutoService();
        OrderService orderService = new OrderService(autoService);

        printTitle("1. Реєстрація клієнта, автомобілів і механіків");
        Client client = new Client("Іван Петренко", "+380501234567");
        Car firstCar = new Car("WVWZZZ1JZXW000001", "Volkswagen", "Golf", 2015, client);
        Car secondCar = new Car("WBAZZZ5A0XW000002", "BMW", "X5", 2019, client);
        Mechanic firstMechanic = new Mechanic(1, "Олег", Specialization.ENGINE);
        Mechanic secondMechanic = new Mechanic(2, "Андрій", Specialization.ELECTRICAL);
        autoService.registerClient(client);
        autoService.registerCar(firstCar);
        autoService.registerCar(secondCar);
        autoService.registerMechanic(firstMechanic);
        autoService.registerMechanic(secondMechanic);
        System.out.println("Клієнт: " + client.getName() + ", " + client.getPhone());
        System.out.println("Автомобіль: " + firstCar.getDetails());
        System.out.println("Автомобіль: " + secondCar.getDetails());

        printTitle("2. Повний життєвий цикл замовлення");
        Order firstOrder = orderService.createOrder(firstCar, Priority.NORMAL);
        printStatus(firstOrder);
        WorkItem oilChange = new WorkItem("Заміна масла", new BigDecimal("50"), new BigDecimal("30"));
        WorkItem brakePads = new WorkItem("Заміна гальмівних колодок", new BigDecimal("120"), new BigDecimal("80"));
        orderService.addWork(firstOrder, oilChange);
        orderService.addWork(firstOrder, brakePads);
        orderService.diagnose(firstOrder);
        printStatus(firstOrder);
        orderService.approve(firstOrder);
        printStatus(firstOrder);
        orderService.assignMechanic(firstOrder, firstMechanic);
        orderService.start(firstOrder);
        printStatus(firstOrder);
        orderService.completeWork(firstOrder, oilChange);
        orderService.completeWork(firstOrder, brakePads);
        orderService.complete(firstOrder);
        printStatus(firstOrder);

        printTitle("3. Підсумкова вартість");
        System.out.println("Вартість звичайного замовлення: " + orderService.calculateTotalCost(firstOrder));

        printTitle("4. Заборонений перехід (CREATED -> APPROVED)");
        Order secondOrder = orderService.createOrder(secondCar, Priority.URGENT);
        expectError(() -> orderService.approve(secondOrder));

        printTitle("5. Погодження порожнього замовлення");
        orderService.diagnose(secondOrder);
        expectError(() -> orderService.approve(secondOrder));

        printTitle("6. Запуск замовлення без механіка");
        orderService.addWork(secondOrder, new WorkItem("Ремонт двигуна", new BigDecimal("200"), new BigDecimal("100")));
        orderService.approve(secondOrder);
        expectError(() -> orderService.start(secondOrder));
        System.out.println("Вартість термінового замовлення: " + orderService.calculateTotalCost(secondOrder));

        printTitle("7. Призначення зайнятого механіка");
        orderService.assignMechanic(secondOrder, firstMechanic);
        Order thirdOrder = orderService.createOrder(firstCar, Priority.NORMAL);
        expectError(() -> orderService.assignMechanic(thirdOrder, firstMechanic));
        orderService.assignMechanic(thirdOrder, secondMechanic);
        System.out.println("Призначено вільного механіка: " + thirdOrder.getMechanic().getName());

        printTitle("8. Зміна завершеного замовлення");
        expectError(() -> orderService.addWork(firstOrder,
                new WorkItem("Додаткова робота", BigDecimal.TEN, BigDecimal.TEN)));

        printTitle("9. Скасування замовлення");
        orderService.cancel(thirdOrder);
        printStatus(thirdOrder);
        expectError(() -> orderService.diagnose(thirdOrder));
    }

    private static void expectError(Runnable action) {
        try {
            action.run();
            System.out.println("ПОМИЛКА: операцію мало бути відхилено!");
        } catch (DomainException exception) {
            System.out.println("Очікувана помилка: " + exception.getMessage());
        }
    }

    private static void printStatus(Order order) {
        System.out.println("Замовлення " + order.getId() + ", статус: " + order.getStatus());
    }

    private static void printTitle(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }
}