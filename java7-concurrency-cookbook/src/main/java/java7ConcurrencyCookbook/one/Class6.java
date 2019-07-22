package java7ConcurrencyCookbook.one;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：1.6线程的睡眠和恢复
 * 替换：[0-9]{1,2}\n
 */
public class Class6 {

    static class FileClock implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.printf("%s\n", new Date());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    System.out.printf("The FileClock has been interrupted");
                }

            }
        }
    }


    @Test
    public void test() {
        FileClock clock = new FileClock();
        Thread thread = new Thread(clock);
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


