package testWork;

import java.util.ArrayList;
import java.util.List;

public class Customer implements Runnable {
    private final List<Store> stores = new ArrayList<>();
    private final int ID;

    public Customer(int ID, List<Store> stores) {
        this.ID = ID;
        this.stores.addAll(stores);
    }

    @Override
    public void run() {
        boolean cond = true;
        while (cond) {
            for (int i = 0; i < stores.size(); i++) {
                if (stores.get(i).newService(ID)) {
                    cond = false;
                }
            }
        }
    }
}
