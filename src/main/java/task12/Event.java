package task12;

import java.time.LocalDateTime;

public class Event {
    private final String type;
    private final String payload;
    private final LocalDateTime createdAt;

    public Event(String type, String payload) {
        this.type = type;
        this.payload = payload;
        this.createdAt = LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return String.format("Event[%s, %s] @ %s", type, payload, createdAt.toLocalTime());
    }
}
