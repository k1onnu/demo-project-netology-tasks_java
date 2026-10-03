package task8;

public class HttpResult {
    private final String url;
    private final int statusCode;      // -1 при ошибке
    private final long responseTimeMs;
    private final String error;        // null, если успех

    public HttpResult(String url, int statusCode, long responseTimeMs, String error) {
        this.url = url;
        this.statusCode = statusCode;
        this.responseTimeMs = responseTimeMs;
        this.error = error;
    }

    public String getUrl() { return url; }
    public int getStatusCode() { return statusCode; }
    public long getResponseTimeMs() { return responseTimeMs; }
    public String getError() { return error; }
    public boolean isSuccess() { return error == null; }

    @Override
    public String toString() {
        if (error != null) {
            return String.format("%-50s ERROR: %s (%d ms)", url, error, responseTimeMs);
        }
        return String.format("%-50s HTTP %d (%d ms)", url, statusCode, responseTimeMs);
    }
}
