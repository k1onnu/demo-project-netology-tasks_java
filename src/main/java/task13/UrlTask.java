package task13;

import java.time.LocalDateTime;

public class UrlTask {
    private final String url;
    private final LocalDateTime createdAt;
    private final int id;

    public UrlTask(int id, String url) {
        this.id = id;
        this.url = url;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }
    public String getUrl() {
        return url;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Task#" + id + " " + url;
    }
}
