package java7ConcurrencyCookbook.one;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：1.7等待线程的终结
 * 替换：[0-9]{1,2}\n
 */
public class Class7 {

    static class DataSourcesLoader implements Runnable {
        @Override
        public void run() {
            System.out.printf("Data sources loading has finished:%s\n", new Date());
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Data sources loading has finished:%s\n", new Date());
        }
    }

    static class NetworkConnectionsLoader implements Runnable {
        @Override
        public void run() {
            System.out.printf("Network Connections Loader loading has finished:%s\n", new Date());
            try {
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Network Connections Loader loading has finished:%s\n", new Date());
        }
    }


    @Test
    public void test() {
        DataSourcesLoader dsLoader = new DataSourcesLoader();
        Thread thread1 = new Thread(dsLoader, "DataSourceThread");

        NetworkConnectionsLoader ncLoader = new NetworkConnectionsLoader();
        Thread thread2 = new Thread(ncLoader, "NetworkConnectionLoader");
        thread1.start();
        thread2.start();

        try {
            //开始等待两个线程完成
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Main: Configuration has been loaded: %s\n", new Date());
    }


}


