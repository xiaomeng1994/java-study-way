package java7ConcurrencyCookbook.two;

import org.junit.Test;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：2.6使用读/写锁同步数据访问
 * 替换：
 */
public class Class6 {

    class PricesInfo {

        private double price1;
        private double price2;

        private ReadWriteLock lock;

        public PricesInfo() {
            price1 = 1.0;
            price2 = 2.0;
            lock = new ReentrantReadWriteLock();
        }

        public double getPrice1() {
            lock.readLock().lock();
            double value = price1;
            lock.readLock().unlock();
            return value;
        }

        public double getPrice2() {
            lock.readLock().lock();
            double value = price2;
            lock.readLock().unlock();
            return value;
        }

        public void setPrices(double price1, double price2) {
            lock.writeLock().lock();
            this.price1 = price1;
            this.price2 = price2;
            lock.writeLock().unlock();
        }
    }

    class Reader implements Runnable {

        private PricesInfo pricesInfo;

        public Reader(PricesInfo pricesInfo) {
            this.pricesInfo = pricesInfo;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.printf("%s: Price 1: %f\n", Thread.
                        currentThread().getName(), pricesInfo.getPrice1());
                System.out.printf("%s: Price 2: %f\n", Thread.
                        currentThread().getName(), pricesInfo.getPrice2());
            }
        }

    }

    class Writer implements Runnable {
        private PricesInfo pricesInfo;

        public Writer(PricesInfo pricesInfo) {
            this.pricesInfo = pricesInfo;
        }

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                System.out.println("Writer: Attempt to modify the prices.");
                pricesInfo.setPrices(Math.random() * 10, Math.random() * 8);
                System.out.println("Writer: Prices have been modified.");
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Test
    public void test() {
        PricesInfo pricesInfo = new PricesInfo();
        Reader[] readers = new Reader[5];
        Thread[] threadsReader = new Thread[5];
        for (int i = 0; i < 5; i++) {
            readers[i] = new Reader(pricesInfo);
            threadsReader[i] = new Thread(readers[i]);
        }
        Writer writer = new Writer(pricesInfo);
        Thread threadWriter = new Thread(writer);
        for (int i = 0; i < 5; i++) {
            threadsReader[i].start();
        }
        threadWriter.start();

        try {
            for (int i = 0; i < 5; i++) {
                threadsReader[i].join();
            }
            threadWriter.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}


