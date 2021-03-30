package com.cw;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) {
        Random rnd=new Random(System.currentTimeMillis());
        Storage.view();
        ReentrantLock lock = new ReentrantLock();
        ExecutorService executor= Executors.newFixedThreadPool(3);
        executor.submit(()->{
            int amountProds=Storage.countProductsInStorage;
            while(amountProds>0){
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(Storage.queue.size()>0) {
                    Storage.getOrder();
                    amountProds = Storage.countProductsInStorage;
                }
            }
        });
        Runnable addOrder=()->{
            lock.lock();
            try {
                int amountProds=Storage.countProductsInStorage;
                while (amountProds > 0) {
                    sleep(100);
                    Integer i=rnd.nextInt(10)+1;
                    Map<Integer, Integer> p=new HashMap<Integer, Integer>();
                    p.put(rnd.nextInt(10)+1,rnd.nextInt(10)+1);
                    Storage.putOrder(p);
                    amountProds = Storage.countProductsInStorage;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        };
        executor.submit(addOrder);
        executor.submit(addOrder);


        executor.shutdown();
    }
}
