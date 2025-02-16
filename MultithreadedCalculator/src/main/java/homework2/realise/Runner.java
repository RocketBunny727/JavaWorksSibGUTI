package homework2.realise;

import java.util.ArrayList;
import java.util.List;

public  class Runner {
    public static void main(String[] args) throws InterruptedException {
         final int THREAD_COUNT = 15;
         final int CALCULATION_LENGTH = 20;
         List<Thread> threads = new ArrayList<>();

         System.out.printf("\033[H");
         System.out.println("\nWelcome!!!");
         for (int i = 0; i < THREAD_COUNT; i++) {
             int complexity = (int) (Math.random() * 10) + 1;
             complexity *= 100;
             Thread thread = new Thread(new Calculator( THREAD_COUNT,i, CALCULATION_LENGTH, complexity));
             threads.add(thread);
             thread.start();
         }

        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("All threads completed and closed.      ");
    }
}
