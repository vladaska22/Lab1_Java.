public class OrderIdGenerator {

    private static final String PREFIX = "ORD-";

    private int lastNumber;

    public String next() {
        lastNumber++;
        return PREFIX + lastNumber;
    }
}