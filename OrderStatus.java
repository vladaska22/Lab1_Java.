public enum OrderStatus {
    CREATED, DIAGNOSED, APPROVED, IN_PROGRESS, COMPLETED, CANCELLED;

    /**
     * Describes the allowed order life cycle.
     */
    public boolean canMoveTo(OrderStatus next) {
        return switch (this) {
            case CREATED -> next == DIAGNOSED || next == CANCELLED;
            case DIAGNOSED -> next == APPROVED || next == CANCELLED;
            case APPROVED -> next == IN_PROGRESS || next == CANCELLED;
            case IN_PROGRESS -> next == COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }

    public boolean isClosed() {
        return this == COMPLETED || this == CANCELLED;
    }
}