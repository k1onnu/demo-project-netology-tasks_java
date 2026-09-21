package task4;

public class Contact {
    private final String sourceUrl;
    private final String type;
    private final String value;

    public Contact(String sourceUrl, String type, String value) {
        this.sourceUrl = sourceUrl;
        this.type = type;
        this.value = value;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s = %s", sourceUrl, type, value);
    }
}

