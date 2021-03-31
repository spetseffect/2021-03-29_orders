package com.cw;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Storage {
    public static ArrayList<Product> list;
    public static Queue<Product> queue;
    public static ReentrantLock lock;
    public static int countProductsInStorage;

    static {
        lock = new ReentrantLock();
        queue = new LinkedList<>();
        list = new ArrayList<>(10);
        Random rnd = new Random(System.currentTimeMillis());
        countProductsInStorage = 0;
        for (int i = 0; i < 10; i++) {
            Product p = new Product(i + 1, "prod_" + i, rnd.nextInt(10));
            list.add(p);
            countProductsInStorage += p.count;
        }
    }

    public static void view() {
        for (Product p :
                list) {
            System.out.println("name:" + p.name + ", count:" + p.count);
        }
    }

    public static void getOrder() {
        Product p = queue.poll();
        if (p != null) {
            for (Product product : list) {
                if (product.id.equals(p.id)) {
                    if(product.count >= p.count) {
                        product.count -= p.count;
                        System.out.println("Обработан заказ: name:" + p.name + ", count:" + p.count);
                    }else if(product.count >0){
                        System.out.println("Обработан заказ: name:" + p.name + ", count:" + p.count+". Выдано только"+product.count+" шт.");
                        list.remove(product);
                    }else System.out.println("В обработке отказано. Продукт "+p.name+" закончился.");
                    break;
                }
            }
        }
    }

    public static void putOrder(Map<Integer, Integer> order) {
        for (var entry :
                order.entrySet()) {
            for (Product product : list) {
                if (product.id.equals(entry.getKey())) {
                    System.out.print("Заказано " + product.name + " " + entry.getValue() + " шт. На выдачу ");
                    if (product.count >= entry.getValue()) {
                        System.out.println(entry.getValue() + " шт.");
                        queue.add(new Product(product.id, product.name, entry.getValue()));
                        countProductsInStorage -= entry.getValue();
                    } else if (product.count > 0) {
                        System.out.println(product.count + " шт.");
                        queue.add(new Product(product.id, product.name, product.count));
                        countProductsInStorage -= product.count;
                    } else {
                        System.out.println("0 шт. Закончился.");
                        list.remove(product);
                    }
                    break;
                }
            }
        }

    }
}
