import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class BlockingQueue {

    private final Queue<String> queue;
    private final Map<Thread, Integer> readIntensityMap = new ConcurrentHashMap<>();

    public BlockingQueue() {
        queue = new LinkedList<>();
    }

    public void add(String elem) {
        synchronized (queue) {
            for (int i = 0; i < 5; i++) {
                this.queue.add(elem);
            }
            queue.notifyAll();
            System.out.println("supplied 5 elements to queue");
            try {
                queue.wait(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String get() {
        String nextElem;
        synchronized (queue) {
            try {
                while (queue.isEmpty()) {
                    queue.wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            nextElem = this.queue.poll();
        }
        readIntensityMap.merge(Thread.currentThread(), 1, Integer::sum);
        return nextElem;
    }

    public void printIntensity() {
        for (Map.Entry<Thread, Integer> threadIntensity : readIntensityMap.entrySet()) {
            System.out.printf("thread %s read %d records\n", threadIntensity.getKey().getName(), threadIntensity.getValue());
        }
    }
}