package testWork;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Store {
    private final Lock lock = new ReentrantLock();
    private final int NUMBER_OF_STORE;

    public Store(int numberOfStore) {
        NUMBER_OF_STORE = numberOfStore;
    }

    public boolean newService(int id) {
        if (lock.tryLock()) {
            try {
                System.out.println(String.format("Store: %d client: %d", NUMBER_OF_STORE, id));
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println(String.format("Store done: %d client: %d", NUMBER_OF_STORE, id));
                lock.unlock();
            }
            return true;
        }
        else {
            return false;
        }
    }

}
