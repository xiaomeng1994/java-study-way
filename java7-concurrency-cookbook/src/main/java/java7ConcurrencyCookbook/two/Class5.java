package java7ConcurrencyCookbook.two;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：2.5使用Lock同步代码块
 * 替换：[0-9]{1,2}\n
 */
public class Class5 {

    class PrintQueue {

        private final Lock queueLock = new ReentrantLock();

        public void printJob(Object document) {
            queueLock.lock();
            try {
                Long duration = (long) (Math.random() * 10000);
                System.out.println(Thread.currentThread().getName() + ": PrintQueue: Printing a Job during " +
                        (duration / 1000) + " seconds");

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
            System.out.printf("%s: Going to print a document\n", Thread.
                    currentThread().getName());
            printQueue.printJob(new Object());
            System.out.printf("%s: The document has been printed\n",
                    Thread.currentThread().getName());
        }
    }

    @Test
    public void test() {
        PrintQueue printQueue = new PrintQueue();
        Thread thread[] = new Thread[10];
        for (int i = 0; i < 10; i++) {
            thread[i] = new Thread(new Job(printQueue), "Thread " + i);
            thread[i].start();
        }

        try {
            for (int i = 0; i < 10; i++) {
                thread[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}


