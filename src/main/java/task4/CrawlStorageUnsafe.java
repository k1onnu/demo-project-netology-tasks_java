package task4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class CrawlStorageUnsafe {
    private final Queue<String> pendingUrl = new LinkedList<>();
    private final Set<String> visitedUrls = new HashSet<>();
    private final List<Contact> contacts = new ArrayList<>();
    private int pagesProcessed = 0;
    private final int maxPages;

    public CrawlStorageUnsafe(int maxPages) {
        this.maxPages = maxPages;
    }

    public void addUrl(String url) {
        if (url == null || url.isBlank()) return;
        if (pagesProcessed >= maxPages) return;
        pendingUrl.add(url);
    }

    public String pollUrl() {
        if (pendingUrl.isEmpty() || pagesProcessed >= maxPages) {
            return null;
        }
        return pendingUrl.poll();
    }

    public boolean markVisitedIfNew(String url) {
        if (visitedUrls.contains(url)) return false;
        visitedUrls.add(url);
        return true;
    }

    public void collectContact(Contact c) {
        if (c == null) return;
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        contacts.add(c);
    }

    public void incrementProcessed() {
        int temp = pagesProcessed;
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        pagesProcessed = temp + 1;
    }

    public void workDone() {
    }

    public int getPagesProcessed() { return pagesProcessed; }
    public int getContactsCount() { return contacts.size(); }
}
