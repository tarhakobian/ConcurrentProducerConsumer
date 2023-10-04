public class Main {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue bq = new BlockingQueue();

        new Thread(() -> {
            while (true) {
                bq.add("" + Math.random());
            }
        }).start();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                while (true) {
                    System.out.printf("thread %s got from blocking queue %s\n",
                            Thread.currentThread().getName(),
                            bq.get());
                }
            }, "" + i).start();
        }

        Thread.sleep(10000);
        bq.printIntensity();
    }
}