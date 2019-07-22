package java7ConcurrencyCookbook.one;

import org.junit.Test;

import java.util.Random;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：1.12处理线程组内的不受控制异常
 * 替换：[0-9]{1,2}\n
 */
public class Class12 {


    static class MyThreadGroup extends ThreadGroup {

        public MyThreadGroup(String name) {
            super(name);
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.printf("The thread %s has thrown an Exception\n", t.getId());
            e.printStackTrace(System.out);
            System.out.printf("Terminating the rest of the Threads\n");
            interrupt();

        }
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            int result;
            Random random = new Random(Thread.currentThread().getId());
            while (true) {
                double nextDouble = random.nextDouble();
                int by = (int) (nextDouble * 1000);
                System.out.printf("thread：%s，nextDouble：%f，by： %d，", Thread.currentThread().getId(), nextDouble, by);
                result = 1000 / by;
                System.out.printf("result: %d\n", result);
                if (Thread.currentThread().isInterrupted()) {
                    System.out.printf("%d : Interrupted\n", Thread.currentThread().getId());
                    return;
                }
            }

        }
    }

    @Test
    public void test() {
        MyThreadGroup threadGroup = new MyThreadGroup("MyThreadGroup");
        Task task = new Task();
        for (int i = 0; i < 2; i++) {
            Thread t = new Thread(threadGroup, task);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}


