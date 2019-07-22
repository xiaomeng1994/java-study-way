package java7ConcurrencyCookbook.one;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：1.10使用本地线程变量
 * 替换：[0-9]{1,2}\n
 */
public class Class10 {


    /**
     * 非安全属性
     */
    static class UnsafeTask implements Runnable {

        private Date startDate;

        @Override
        public void run() {
            startDate = new Date();
            System.out.printf("Starting Thread: %s : %s\n", Thread.currentThread().getId(), startDate);
            try {
                TimeUnit.SECONDS.sleep((int) Math.rint(Math.random() * 10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Thread Finished: %s : %s\n", Thread.currentThread().getId(), startDate);
        }

    }

    /**
     * 安全属性
     */
    static class SafeTask implements Runnable {

        private static ThreadLocal<Date> startDate = ThreadLocal.withInitial(Date::new);

        @Override
        public void run() {
            System.out.printf("Starting Thread: %s : %s\n", Thread.currentThread().getId(), startDate.get());
            try {
                TimeUnit.SECONDS.sleep((int) Math.rint(Math.random() * 10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Thread Finished: %s : %s\n", Thread.currentThread().getId(), startDate.get());
        }


    }

    @Test
    public void test() throws InterruptedException {
        System.out.println("------------------------非安全开始--------------------------");
        Thread[] arr = new Thread[3];
        UnsafeTask unsafeTask = new UnsafeTask();
        for (int i = 0; i < arr.length; i++) {
            Thread thread = new Thread(unsafeTask);
            thread.start();
            TimeUnit.SECONDS.sleep(1);
            arr[i] = thread;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i].join();
        }
        System.out.println("------------------------安全开始--------------------------");
        SafeTask safeTask = new SafeTask();
        for (int i = 0; i < arr.length; i++) {
            Thread thread = new Thread(safeTask);
            thread.start();
            TimeUnit.SECONDS.sleep(2);
            arr[i] = thread;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i].join();
        }
    }

}


