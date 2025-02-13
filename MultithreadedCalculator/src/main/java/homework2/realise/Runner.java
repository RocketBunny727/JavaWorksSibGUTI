package homework2.realise;

public  class Runner {
    public static void main(String[] args) {
         final int THREAD_COUNT = 5;
         final int CALCULATION_LENGTH = 20;

        System.out.println("Welcome!!!");
         for (int i = 0; i < THREAD_COUNT; i++) {
             int complexity = (int) (Math.random() * 10) + 1;
             complexity *= 100;
             Thread thread = new Thread(new Calculator(i + 1, CALCULATION_LENGTH, complexity));
             thread.start();
         }
    }
}
