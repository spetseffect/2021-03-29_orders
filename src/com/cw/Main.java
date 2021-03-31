package com.cw;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) {
        Random rnd=new Random(System.currentTimeMillis());
        Storage.view();

        ExecutorService executor= Executors.newFixedThreadPool(3);
        Runnable addOrder=()->{
            Storage.lock.lock();
            try {
                int amountProds=Storage.countProductsInStorage;
                while (amountProds > 0) {
                    sleep(50);
                    Map<Integer, Integer> p= new HashMap<>();
                    p.put(rnd.nextInt(10)+1,rnd.nextInt(10)+1);
                    Storage.putOrder(p);
                    amountProds = Storage.countProductsInStorage;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Storage.lock.unlock();
            }
        };
        executor.submit(addOrder);
        executor.submit(addOrder);
        executor.submit(()->{
            int amountProds=Storage.countProductsInStorage;
            int amountOrders=Storage.queue.size();
            while(amountProds>0 || amountOrders>0){
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(Storage.queue.size()>0) {
                    Storage.getOrder();
                    amountProds = Storage.countProductsInStorage;
                    amountOrders--;
                }
            }
        });
        executor.shutdown();
        while(!executor.isTerminated()){
            try {
//                System.out.println("Идёт процесс.");
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Остатки на складе:");
        Storage.view();
        System.out.println("Storage.queue.size() = "+Storage.queue.size());
        System.out.println("Storage.countProductsInStorage = "+Storage.countProductsInStorage);
    }
}
