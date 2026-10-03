package task8;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    private static final List<String> URLS = List.of(
            "https://api.github.com",
            "https://api.github.com/users/octocat",
            "https://jsonplaceholder.typicode.com/posts/1",
            "https://jsonplaceholder.typicode.com/users",
            "https://jsonplaceholder.typicode.com/todos/1",
            "https://httpbin.org/get",
            "https://httpbin.org/status/200",
            "https://httpbin.org/status/404",
            "https://httpbin.org/delay/1",
            "https://api.publicapis.org/entries",
            "https://catfact.ninja/fact",
            "https://dog.ceo/api/breeds/list/all",
            "https://official-joke-api.appspot.com/random_joke",
            "https://api.chucknorris.io/jokes/random",
            "https://api.agify.io?name=michael",
            "https://api.genderize.io?name=alice",
            "https://api.nationalize.io?name=smith",
            "https://randomuser.me/api/",
            "https://api.ipify.org?format=json",
            "https://api.coindesk.com/v1/bpi/currentprice.json"
    );

    public static void main(String[] args) throws InterruptedException {
        int corePoolSize = 4;
        int maxPoolSize = 8;
        long keepAliveSeconds = 30;
        int queueCapacity = 100;

        // Ручная настройка ThreadPoolExecutor
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueCapacity),
                new ThreadFactory() {
                    private int counter = 0;
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r, "http-worker-" + counter++);
                        t.setDaemon(false);
                        return t;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        // Один HttpClient на всё приложение
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        System.out.println("=== ThreadPoolExecutor config ===");
        System.out.println("corePoolSize:   " + corePoolSize);
        System.out.println("maxPoolSize:    " + maxPoolSize);
        System.out.println("keepAlive:      " + keepAliveSeconds + "s");
        System.out.println("queueCapacity:  " + queueCapacity);
        System.out.println("URLs:           " + URLS.size());
        System.out.println();

        long start = System.nanoTime();

        // Отправляем все задачи в пул, получаем Futures
        List<Future<HttpResult>> futures = new ArrayList<>();
        for (String url : URLS) {
            futures.add(executor.submit(new HttpChecker(url, client)));
        }

        // Собираем результаты
        List<HttpResult> results = new ArrayList<>();
        for (Future<HttpResult> future : futures) {
            try {
                results.add(future.get(15, TimeUnit.SECONDS));
            } catch (ExecutionException | TimeoutException e) {
                System.err.println("Task failed: " + e.getMessage());
            }
        }

        long totalTime = (System.nanoTime() - start) / 1_000_000;

        // Сводка
        printSummary(results, totalTime);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    private static void printSummary(List<HttpResult> results, long totalTimeMs) {
        System.out.println("=== Results ===");
        for (HttpResult r : results) {
            System.out.println(r);
        }

        long success = results.stream().filter(HttpResult::isSuccess).count();
        long failed = results.size() - success;
        double avgTime = results.stream()
                .mapToLong(HttpResult::getResponseTimeMs)
                .average().orElse(0);
        long maxTime = results.stream()
                .mapToLong(HttpResult::getResponseTimeMs)
                .max().orElse(0);
        long minTime = results.stream()
                .mapToLong(HttpResult::getResponseTimeMs)
                .min().orElse(0);

        System.out.println();
        System.out.println("=== Summary ===");
        System.out.println("Total URLs:      " + results.size());
        System.out.println("Success:         " + success);
        System.out.println("Failed:          " + failed);
        System.out.println("Avg response:    " + String.format("%.2f", avgTime) + " ms");
        System.out.println("Min response:    " + minTime + " ms");
        System.out.println("Max response:    " + maxTime + " ms");
        System.out.println("Total wall time: " + totalTimeMs + " ms");

        // Распределение по статус-кодам
        System.out.println();
        System.out.println("=== Status codes ===");
        results.stream()
                .filter(HttpResult::isSuccess)
                .collect(java.util.stream.Collectors.groupingBy(
                        HttpResult::getStatusCode,
                        java.util.stream.Collectors.counting()))
                .forEach((code, count) -> System.out.println("HTTP " + code + ": " + count));
    }
}


/*
* === ThreadPoolExecutor config ===
corePoolSize:   4
maxPoolSize:    8
keepAlive:      30s
queueCapacity:  100
URLs:           20

=== Results ===
https://api.github.com                             HTTP 200 (443 ms)
https://api.github.com/users/octocat               HTTP 200 (443 ms)
https://jsonplaceholder.typicode.com/posts/1       HTTP 200 (443 ms)
https://jsonplaceholder.typicode.com/users         HTTP 200 (443 ms)
https://jsonplaceholder.typicode.com/todos/1       HTTP 200 (48 ms)
https://httpbin.org/get                            HTTP 200 (581 ms)
https://httpbin.org/status/200                     HTTP 200 (580 ms)
https://httpbin.org/status/404                     HTTP 404 (579 ms)
https://httpbin.org/delay/1                        HTTP 200 (1612 ms)
https://api.publicapis.org/entries                 ERROR: ConnectException (651 ms)
https://catfact.ninja/fact                         HTTP 200 (349 ms)
https://dog.ceo/api/breeds/list/all                HTTP 200 (240 ms)
https://official-joke-api.appspot.com/random_joke  HTTP 200 (300 ms)
https://api.chucknorris.io/jokes/random            HTTP 200 (260 ms)
https://api.agify.io?name=michael                  ERROR: HttpConnectTimeoutException (5008 ms)
https://api.genderize.io?name=alice                ERROR: HttpConnectTimeoutException (5004 ms)
https://api.nationalize.io?name=smith              ERROR: HttpConnectTimeoutException (5010 ms)
https://randomuser.me/api/                         HTTP 200 (296 ms)
https://api.ipify.org?format=json                  ERROR: HttpConnectTimeoutException (5012 ms)
https://api.coindesk.com/v1/bpi/currentprice.json  ERROR: ConnectException (566 ms)

=== Summary ===
Total URLs:      20
Success:         14
Failed:          6
Avg response:    1393.40 ms
Min response:    48 ms
Max response:    5012 ms
Total wall time: 7415 ms

=== Status codes ===
HTTP 404: 1
HTTP 200: 13
* */
