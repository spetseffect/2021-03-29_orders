package com.cw;

import java.util.*;

public class Storage {
    public static ArrayList<Product> list;
    public static Queue<Product> queue;
    public static int countProductsInStorage;

    static {
        queue = new LinkedList<>();
        list = new ArrayList<>(10);
        Random rnd = new Random(System.currentTimeMillis());
        countProductsInStorage = 0;
        for (int i = 0; i < 10; i++) {
            Product p = new Product(i + 1, "prod" + i, rnd.nextInt(10));
            list.add(p);
            countProductsInStorage += p.count;
        }
    }

    public static void view() {
        for (Product p :
                list) {
            System.out.println("id:" + p.id + ", name:" + p.name + ", count:" + p.count);
        }
    }

    public static void getOrder() {
        Product p = queue.poll();
        if (p != null) {
            countProductsInStorage -= p.count;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id == p.id) {
                    list.get(i).count -= p.count;
                    System.out.println("Обработан заказ: id:" + p.id + ", name:" + p.name + ", count:" + p.count);
                    break;
                }
            }
        }
    }

    public static void putOrder(Map<Integer, Integer> order) {
        for (var entry :
                order.entrySet()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id.equals(entry.getKey())) {
                    System.out.print("Заказано " + list.get(i).name + " " + entry.getValue() + " шт. На выдачу ");
                    if (list.get(i).count >= entry.getValue()) {
                        System.out.println(entry.getValue() + " шт.");
                        queue.add(new Product(list.get(i).id, list.get(i).name, entry.getValue()));
                    } else if (list.get(i).count > 0) {
                        System.out.println(list.get(i).count + " шт.");
                        queue.add(new Product(list.get(i).id, list.get(i).name, list.get(i).count));
                    } else System.out.println("0 шт. Закончился.");
                    break;
                }
            }
        }

    }
}
