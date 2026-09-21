package task4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class CrawlStorage {
    private final Queue<String> pendingUrl = new LinkedList<>();
    private final Set<String> visitedUrls = new HashSet<>();
    private final List<Contact> contacts = new ArrayList<>();
    private int pagesProcessed = 0;
    private int pendingCount = 0;
    private final Object lock = new Object();
    private final int maxPages;

    public CrawlStorage(int maxPages) {
        this.maxPages = maxPages;
    }

    public void addUrl(String url) {
        synchronized (lock) {
            if (url == null || url.isBlank()) return;
            if (pagesProcessed >= maxPages) return;
            pendingUrl.add(url);
            pendingCount++;
            lock.notifyAll();
        }
    }

    public String pollUrl() throws InterruptedException {
        synchronized (lock) {
            while (pendingUrl.isEmpty() && pendingCount > 0 && pagesProcessed < maxPages) {
                lock.wait();
            }
            if (pendingUrl.isEmpty() || pagesProcessed >= maxPages) {
                return null;
            }
            return pendingUrl.poll();
        }
    }

    public boolean markVisitedIfNew(String url) {
        synchronized (lock) {
            return visitedUrls.add(url);
        }
    }

    public void collectContact(Contact c) {
        synchronized (lock) {
            if (c == null) return;
            contacts.add(c);
        }
    }

    public void incrementProcessed() {
        synchronized (lock) {
            pagesProcessed++;
            if (pagesProcessed >= maxPages) {
                lock.notifyAll();
            }
        }
    }

    public void workDone() {
        synchronized (lock) {
            pendingCount--;
            lock.notifyAll();
        }
    }

    public int getPagesProcessed() {
        synchronized (lock) { return pagesProcessed; }
    }

    public int getContactsCount() {
        synchronized (lock) { return contacts.size(); }
    }
}
