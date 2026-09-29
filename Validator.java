import java.math.BigDecimal;

/**
 * Shared argument checks, so entities stay free of validation code.
 */
public class Validator {

    private Validator() {
    }

    public static <T> T requireNonNull(T value, String message) {
        if (value == null) {
            throw new DomainException(message);
        }
        return value;
    }

    public static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new DomainException(message);
        }
        return value.trim();
    }

    public static BigDecimal requireNonNegative(BigDecimal value, String message) {
        requireNonNull(value, message);
        if (value.signum() < 0) {
            throw new DomainException(message);
        }
        return value;
    }

    public static long requireAtLeast(long value, long minimum, String message) {
        if (value < minimum) {
            throw new DomainException(message);
        }
        return value;
    }
}