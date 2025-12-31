package enums;

public enum TaskType {

    ADD("add"),
    DELETE("delete"),
    UPDATE("update"),
    LIST("list"),
    MARK_IN_PROGRESS("mark-in-progress"),
    MARK_DONE("mark-done");

    private final String value;

    TaskType(String value) {
        this.value = value;
    }

    public static TaskType convert(String value) {
        for (TaskType type : TaskType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown TaskType value: " + value);
    }
}
