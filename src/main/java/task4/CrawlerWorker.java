package task4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrawlerWorker implements Runnable {
    private final CrawlStorage storage;
    private final Random random = new Random();

    public CrawlerWorker(CrawlStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String url = storage.pollUrl();
                if (url == null) break;

                try {
                    if (!storage.markVisitedIfNew(url)) continue;

                    String html = fetchPage(url);
                    for (Contact c : parseContacts(url, html)) {
                        storage.collectContact(c);
                    }
                    for (String link : parseLinks(url, html)) {
                        storage.addUrl(link);
                    }
                    storage.incrementProcessed();
                } finally {
                    storage.workDone();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String fetchPage(String url) {
        double sum = 0;
        for (int i = 0; i < 2_000_000; i++) {
            sum += Math.sin(i) * Math.cos(i);
        }
        return "HTML of " + url + " " + sum;
    }

    private List<Contact> parseContacts(String url, String html) {
        int count = 1 + random.nextInt(3);
        List<Contact> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(new Contact(url, "phone",
                    "+7 (900)-" + String.format("%06d", random.nextInt(1_000_000))));
        }
        return result;
    }

    private List<String> parseLinks(String url, String html) {
        int count = 1 + random.nextInt(3);
        List<String> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(url + "/page" + random.nextInt(100));
        }
        return result;
    }
}
