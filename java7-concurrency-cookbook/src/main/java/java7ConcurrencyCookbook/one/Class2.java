package java7ConcurrencyCookbook.one;

import lombok.AllArgsConstructor;
import org.junit.Test;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：1.2线程的创建和运行
 * 替换：[0-9]{1,2}\n
 */
public class Class2 {

    @AllArgsConstructor
    static class Calculator implements Runnable {

        private int number;

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                System.out.printf("%s: %d * %d = %d\n", Thread.currentThread().getName(), number, i, i * number);
            }
        }
    }


    @Test
    public void test() {
        for (int i = 1; i <= 10; i++) {
            Calculator calculator = new Calculator(i);
            Thread thread = new Thread(calculator);
            thread.start();
        }

    }

}


