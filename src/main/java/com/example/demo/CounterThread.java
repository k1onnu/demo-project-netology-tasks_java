package com.example.demo;

public class CounterThread extends Thread {
    private final int amt;

    public CounterThread(String name, int amt) {
        super(name);
        this.amt = amt;
    }

    @Override
    public void run() {
        for (int i = 1; i <= amt; i++) {
            System.out.println(getName() + " - " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
