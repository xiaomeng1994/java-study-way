package java7ConcurrencyCookbook.two;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：2.7使用读/写锁同步数据访问
 * 替换：[0-9]{1,2}\n
 */

public class Class7 {

    class PrintQueue {

        //true公平锁，false非公平锁
        private final Lock queueLock = new ReentrantLock(true);

        @SuppressWarnings("Duplicates")
        public void printJob() {
            queueLock.lock();
            try {
                Long duration = (long) (Math.random() * 10000);
                System.out.println(Thread.currentThread().getName() +
                        ": PrintQueue 1 -> Printing a Job during :" + (duration / 1000) + " seconds ");
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                queueLock.unlock();
            }
            queueLock.lock();
            try {
                Long duration = (long) (Math.random() * 10000);
                System.out.println(Thread.currentThread().getName() +
                        ": PrintQueue 2 -> Printing a Job during :" + (duration / 1000) + " seconds ");
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                queueLock.unlock();
            }
        }
    }

    class Job implements Runnable {

        private PrintQueue printQueue;

        public Job(PrintQueue printQueue) {
            this.printQueue = printQueue;
        }

        @Override
        public void run() {
            System.out.printf("%s: Going to print a document \n", Thread.currentThread().getName());
            printQueue.printJob();
            System.out.printf("%s: The document has been printed \n", Thread.currentThread().getName());
        }
    }


    @Test
    public void test() {
        Job job = new Job(new PrintQueue());
        Thread[] threads = new Thread[10];
        try {
            for (int i = 0; i < 10; i++) {
                threads[i] = new Thread(job, "Thread" + i);
                threads[i].start();
                Thread.sleep(100);
            }
            for (int i = 0; i < 10; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


