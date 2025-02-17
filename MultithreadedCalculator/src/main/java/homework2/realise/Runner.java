package homework2.realise;

public  class Runner {
    public static void main(String[] args) throws InterruptedException {
         final int THREAD_COUNT = 15;
         final int CALCULATION_LENGTH = 20;
         final ThreadsMaker maker = new ThreadsMaker(CALCULATION_LENGTH);

         System.out.print("\033[H");
         System.out.println("\nWelcome!!!");
         maker.createThreads(THREAD_COUNT);
    }
}
