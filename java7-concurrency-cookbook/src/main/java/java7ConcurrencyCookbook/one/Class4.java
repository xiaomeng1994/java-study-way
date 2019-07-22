package java7ConcurrencyCookbook.one;

import org.junit.Test;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：1.4线程的中断
 * 替换：[0-9]{1,2}\n
 */
public class Class4 {

    static class PrimeGenerator extends Thread {
        private boolean temple = false;

        @Override
        public void run() {
            long number = 1L;
            while (true) {
                if (isPrime(number)) {
                    System.out.printf("Number %d is Prime\n", number);
                }
                //使用isInterrupted进行终止线程
                if (isInterrupted()) {
                    System.out.println("The Prime Generator has been Interrupted");
                    return;
                }
                //使用成员变量进行终止线程（此种方法存在风险，线程在执行时会将变量读取到线程的工作内存）
                if (temple) {
                    System.out.println("The Prime Generator has been Interrupted");
                    return;
                }
                number++;
            }
        }

        private boolean isPrime(long number) {
            if (number <= 2) {
                return true;
            }
            for (long i = 2; i < number; i++) {
                if ((number % i) == 0) {
                    return false;
                }
            }
            return true;
        }
    }


    @Test
    public void test() throws Exception {
        Thread task = new PrimeGenerator();
        task.start();
        try {
            Thread.sleep(1000);
            System.out.println("准备通知结束");
//            ((java7ConcurrencyCookbook.one.Class4) task).temple = true;
            task.interrupt();
            System.out.println("完成通知结束");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}


