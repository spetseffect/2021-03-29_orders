package com.tiktak;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Main {
    static String last = "";
    static int count = 0;

    static void Counter(String status) {
        last = status;
        count++;
    }

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Runnable task = () -> {
            while (count < 100) {
                lock.lock();
                try {
                    sleep(100);
                    if (last.equals("tik")) {
                        System.out.println(count + "\ttak");
                        Counter("tak");
                    } else {
                        System.out.println(count + "tik");
                        Counter("tik");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        executor.submit(task);
        executor.submit(task);
        executor.shutdown();
    }
}
