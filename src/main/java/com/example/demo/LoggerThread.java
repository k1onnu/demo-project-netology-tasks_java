package com.example.demo;

public class LoggerThread implements Runnable {
    private final int amt;

    public LoggerThread(int amt) {
        this.amt = amt;
    }

    @Override
    public void run() {
        for (int i = 1; i <= amt; i++) {
            System.out.println(Thread.currentThread().getName() + " - " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
