package java7ConcurrencyCookbook.one;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：1.11线程组
 * 替换：[0-9]{1,2}\n
 */
public class Class11 {

    @Getter
    @Setter
    private static class Result {
        private String name;
    }

    static class SearchTask implements Runnable {

        private Result result;

        public SearchTask(Result result) {
            this.result = result;
        }


        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            System.out.printf("Thread %s: Start\n", name);
            try {
                doTask();
                result.setName(name);
            } catch (InterruptedException e) {
                System.out.printf("Thread %s: Interrupted\n", name);
                return;
            }
            System.out.printf("Thread %s: End\n", name);
        }

        private void doTask() throws InterruptedException {
            Random random = new Random((new Date()).getTime());
            int value = (int) (random.nextDouble() * 100);
            System.out.printf("Thread %s: %d\n", Thread.currentThread().getName(), value);
            TimeUnit.SECONDS.sleep(value);
        }
    }


    @Test
    public void test() {
        ThreadGroup threadGroup = new ThreadGroup("Searcher");
        Result result = new Result();
        SearchTask searchTask = new SearchTask(result);
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(threadGroup, searchTask);
            thread.start();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Number of Threads: %d\n", threadGroup.activeCount());
        System.out.print("Information about the Thread Group\n");
        threadGroup.list();
        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
        for (int i = 0; i < threadGroup.activeCount(); i++) {
            System.out.printf("Thread %s: %s\n", threads[i].getName(), threads[i].getState());
        }
        waitFinish(threadGroup);
        try {
            threadGroup.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void waitFinish(ThreadGroup threadGroup) {
        while (threadGroup.activeCount() > 4) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}


