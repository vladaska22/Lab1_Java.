public class Client {

    private String name;
    private String phone;

    public Client(String name, String phone) {
        this.name = Validator.requireText(name, "Ім'я клієнта не може бути порожнім.");
        this.phone = Validator.requireText(phone, "Телефон клієнта не може бути порожнім.");
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}