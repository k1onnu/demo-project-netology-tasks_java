package task8;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Callable;

public class HttpChecker implements Callable<HttpResult> {
    private final String url;
    private final HttpClient client;

    public HttpChecker(String url, HttpClient client) {
        this.url = url;
        this.client = client;
    }

    @Override
    public HttpResult call() {
        long start = System.nanoTime();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<Void> response = client.send(request,
                    HttpResponse.BodyHandlers.discarding());

            long elapsed = (System.nanoTime() - start) / 1_000_000;
            return new HttpResult(url, response.statusCode(), elapsed, null);

        } catch (Exception e) {
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            return new HttpResult(url, -1, elapsed, e.getClass().getSimpleName());
        }
    }
}
