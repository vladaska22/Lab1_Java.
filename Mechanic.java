public class Mechanic {

    private static final long MIN_ID = 1;

    private long id;
    private String name;
    private Specialization specialization;

    public Mechanic(long id, String name, Specialization specialization) {
        this.id = Validator.requireAtLeast(id, MIN_ID, "Ідентифікатор механіка має бути додатним.");
        this.name = Validator.requireText(name, "Ім'я механіка не може бути порожнім.");
        this.specialization = Validator.requireNonNull(specialization, "Спеціалізація механіка не може бути null.");
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Specialization getSpecialization() {
        return specialization;
    }
}