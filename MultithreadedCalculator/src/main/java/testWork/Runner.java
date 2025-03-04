package testWork;

import java.util.ArrayList;
import java.util.List;

public class Runner {
    public static void main(String[] args) throws InterruptedException {
        final int COUNT_OF_PEOPLES = 10;
        final int COUNT_OF_STORES = 5;
        final List<Store> stores = new ArrayList<>(COUNT_OF_STORES);
        final List<Thread> threads = new ArrayList<>(COUNT_OF_PEOPLES);

        for (int i = 0; i < COUNT_OF_STORES; i++) {
            Store store = new Store(i);
            stores.add(store);
        }

        for (int i = 0; i < COUNT_OF_PEOPLES; i++) {
            Customer customer = new Customer(i, stores);
            Thread thread = new Thread(customer);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("All clients done!");
    }
}
