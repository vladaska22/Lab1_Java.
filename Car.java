public class Car {

    private static final int MIN_YEAR = 1886;
    private static final String DETAILS_FORMAT = "%s %s, %d (%s)";

    private String vin;
    private String brand;
    private String model;
    private int year;
    private Client owner;

    public Car(String vin, String brand, String model, int year, Client owner) {
        this.vin = Validator.requireText(vin, "VIN-код не може бути порожнім.");
        this.brand = Validator.requireText(brand, "Марка автомобіля не може бути порожньою.");
        this.model = Validator.requireText(model, "Модель автомобіля не може бути порожньою.");
        this.year = (int) Validator.requireAtLeast(year, MIN_YEAR, "Некоректний рік випуску автомобіля.");
        this.owner = Validator.requireNonNull(owner, "Автомобіль повинен мати власника.");
    }

    public String getVin() {
        return vin;
    }

    public Client getOwner() {
        return owner;
    }

    public String getDetails() {
        return String.format(DETAILS_FORMAT, brand, model, year, vin);
    }
}