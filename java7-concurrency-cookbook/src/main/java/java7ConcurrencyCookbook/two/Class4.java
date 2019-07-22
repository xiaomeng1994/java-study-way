package java7ConcurrencyCookbook.two;

import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：2.4在同步代码中使用条件
 * 替换：[0-9]{1,2}\n
 */
public class Class4 {

    class EventStorage {

        private int maxSize;
        private List<Date> storage;

        public EventStorage() {
            maxSize = 10;
            storage = new LinkedList<>();
        }

        public synchronized void set() {
            while (storage.size() == maxSize) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            storage.offer(new Date());
            storage.add(new Date());
            System.out.printf("Set: %d\n", storage.size());
            notifyAll();
        }

        public synchronized void get() {
            while (storage.size() == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.printf("Get: %d: %s\n", storage.
                    size(), ((LinkedList<?>) storage).poll());
            notifyAll();
        }
    }

    class Producer implements Runnable {
        private EventStorage storage;

        public Producer(EventStorage storage) {
            this.storage = storage;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                storage.set();
            }
        }

    }

    class Consumer implements Runnable {
        private EventStorage storage;

        public Consumer(EventStorage storage) {
            this.storage = storage;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                storage.get();
            }
        }

    }

    @Test
    public void test() {
        EventStorage storage = new EventStorage();

        Producer producer = new Producer(storage);
        Thread thread1 = new Thread(producer);

        Consumer consumer = new Consumer(storage);
        Thread thread2 = new Thread(consumer);
        thread2.start();
        thread1.start();

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}


