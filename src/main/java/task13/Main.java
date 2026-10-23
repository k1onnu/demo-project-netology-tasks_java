package task13;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        List<String> urls = List.of(
                "https://example.com",
                "https://www.wikipedia.org",
                "https://www.github.com",
                "https://jsonplaceholder.typicode.com",
                "https://httpbin.org/get",
                "https://www.python.org",
                "https://www.java.com",
                "https://www.oracle.com",
                "https://stackoverflow.com",
                "https://www.reddit.com",
                "https://www.bbc.com",
                "https://www.cnn.com",
                "https://www.nytimes.com",
                "https://www.theguardian.com",
                "https://www.reuters.com",
                "https://www.nature.com",
                "https://www.sciencedaily.com",
                "https://www.nasa.gov",
                "https://www.esa.int",
                "https://www.spacex.com"
        );

        int queueCapacity = 5;
        int producerCount = 2;
        int consumerCount = 4;

        TaskServer server = new TaskServer(queueCapacity, producerCount, consumerCount);

        int mid = urls.size() / 2;
        server.submitProducer(urls.subList(0, mid));
        server.submitProducer(urls.subList(mid, urls.size()));

        long start = System.nanoTime();
        server.awaitCompletion();
        long elapsed = (System.nanoTime() - start) / 1_000_000;

        System.out.println();
        System.out.println("URLs total:       " + urls.size());
        System.out.println("Processed:        " + server.getProcessedCount());
        System.out.println("Queue remaining:  " + server.getQueueSize());
        System.out.println("Time:             " + elapsed + " ms");
    }
}
