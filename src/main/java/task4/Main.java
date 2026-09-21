package task4;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int workers = args.length > 0 ? Integer.parseInt(args[0]) : 4;
        boolean useUnsafe = args.length > 1 && args[1].equals("unsafe");
        int maxPages = 500;

        System.out.println("Mode: " + (useUnsafe ? "UNSAFE" : "SAFE"));
        System.out.println("Workers: " + workers);

        long start = System.nanoTime();

        if (useUnsafe) {
            CrawlStorageUnsafe storage = new CrawlStorageUnsafe(maxPages);
            for (int i = 1; i <= 10; i++) {
                storage.addUrl("https://company" + i + ".ru");
            }

            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < workers; i++) {
                threads.add(new Thread(new CrawlerWorkerUnsafe(storage), "worker-" + i));
            }

            for (Thread t : threads) t.start();
            for (Thread t : threads) t.join();

            long end = System.nanoTime();
            System.out.println("Time: " + (end - start) / 1_000_000 + " ms");
            System.out.println("Pages processed: " + storage.getPagesProcessed());
            System.out.println("Contacts collected: " + storage.getContactsCount());
        } else {
            CrawlStorage storage = new CrawlStorage(maxPages);
            for (int i = 1; i <= 10; i++) {
                storage.addUrl("https://company" + i + ".ru");
            }

            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < workers; i++) {
                threads.add(new Thread(new CrawlerWorker(storage), "worker-" + i));
            }

            for (Thread t : threads) t.start();
            for (Thread t : threads) t.join();

            long end = System.nanoTime();
            System.out.println("Time: " + (end - start) / 1_000_000 + " ms");
            System.out.println("Pages processed: " + storage.getPagesProcessed());
            System.out.println("Contacts collected: " + storage.getContactsCount());
        }
    }
}

/*
args: 1

Workers: 1
Time: 40627 ms
Pages processed: 500
Contacts collected: 1003

Workers: 1
Time: 40630 ms
Pages processed: 500
Contacts collected: 994

Workers: 1
Time: 40610 ms
Pages processed: 500
Contacts collected: 1003
--------------------------
args: 2

Workers: 2
Time: 20440 ms
Pages processed: 501
Contacts collected: 1047

Workers: 2
Time: 20420 ms
Pages processed: 501
Contacts collected: 978

Workers: 2
Time: 20473 ms
Pages processed: 501
Contacts collected: 1009
--------------------------
args: 4

Workers: 4
Time: 10865 ms
Pages processed: 503
Contacts collected: 999

Workers: 4
Time: 10663 ms
Pages processed: 503
Contacts collected: 998

Workers: 4
Time: 10660 ms
Pages processed: 503
Contacts collected: 1000
--------------------------
args: 8

Workers: 8
Time: 5795 ms
Pages processed: 507
Contacts collected: 1036

Workers: 8
Time: 5777 ms
Pages processed: 507
Contacts collected: 1023

Workers: 8
Time: 5679 ms
Pages processed: 507
Contacts collected: 1008
--------------------------------------------------------

* */