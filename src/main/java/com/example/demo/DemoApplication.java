package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws InterruptedException {
		CounterThread thread1 = new CounterThread("CounterThread", 5);
		thread1.start();

		Thread thread2 = new Thread(new LoggerThread(5),"LoggerThread");
		thread2.start();

		thread1.join();
		thread2.join();

		System.out.println();
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			System.out.println(t.getName() + " (alive: " + t.isAlive() + ")");
		}
	}
}
