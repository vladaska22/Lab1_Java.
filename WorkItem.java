import java.math.BigDecimal;

public class WorkItem {

    private String description;
    private BigDecimal partsCost;
    private BigDecimal laborCost;
    private boolean completed;

    public WorkItem(String description, BigDecimal partsCost, BigDecimal laborCost) {
        this.description = Validator.requireText(description, "Опис роботи не може бути порожнім.");
        this.partsCost = Validator.requireNonNegative(partsCost, "Вартість запчастин не може бути null або від'ємною.");
        this.laborCost = Validator.requireNonNegative(laborCost, "Вартість роботи не може бути null або від'ємною.");
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPartsCost() {
        return partsCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void complete() {
        completed = true;
    }
}