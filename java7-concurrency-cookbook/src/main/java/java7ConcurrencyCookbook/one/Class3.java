package java7ConcurrencyCookbook.one;

import lombok.AllArgsConstructor;
import org.junit.Test;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：1.3获取和设置线程信息
 * 替换：[0-9]{1,2}\n
 */
public class Class3 {

    @AllArgsConstructor
    static class Calculator implements Runnable {

        private int number;

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep(Math.abs(this.number - 9));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.printf("%s: %d * %d = %d\n", Thread.currentThread().getName(), number, i, i * number);
            }
        }
    }


    @Test
    public void test() throws Exception {
        Thread threads[] = new Thread[10];
        Thread.State status[] = new Thread.State[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Calculator(i));
            if ((i % 2) == 0) {
                threads[i].setPriority(Thread.MAX_PRIORITY);
            } else {
                threads[i].setPriority(Thread.MIN_PRIORITY);
            }
            threads[i].setName("Thread " + i);
        }
        try (FileWriter file = new FileWriter(".\\data\\log.txt"); PrintWriter pw = new PrintWriter(file)) {
            for (int i = 0; i < 10; i++) {
                pw.println("Main : Status of Thread " + i + " : " + threads[i].getState());
                status[i] = threads[i].getState();
            }
            //启动所有线程
            for (int i = 0; i < 10; i++) {
                threads[i].start();
            }
            boolean finish = false;
            //直到这10个线程执行结束，我们会一直检查它们的状态。如果发现它的状态改变，就把状态记入文本。
            while (!finish) {
                for (int i = 0; i < 10; i++) {
                    if (threads[i].getState() != status[i]) {
                        writeThreadInfo(pw, threads[i], status[i]);
                        status[i] = threads[i].getState();
                    }
                }
                finish = true;
                for (int i = 0; i < 10; i++) {
                    //打印每个线程状态的执行结果
                    System.out.printf("%d完成结果：%s\n", i, threads[i].getState() == Thread.State.TERMINATED);
                    finish = finish && (threads[i].getState() == Thread.State.TERMINATED);
                }
            }
        }
    }

    /**
     * 实现一个方法 writeThreadInfo()，这个方法写线程的 ID, name, priority, old status, 和 new status。
     *
     * @param pw
     * @param thread
     * @param state
     */
    private static void writeThreadInfo(PrintWriter pw, Thread thread, Thread.State state) {
        pw.printf("Main : Id %d - %s\n", thread.getId(), thread.getName());
        pw.printf("Main : Priority: %d\n", thread.getPriority());
        pw.printf("Main : Old State: %s\n", state);
        pw.printf("Main : New State: %s\n", thread.getState());
        pw.printf("Main : ************************************\n");
    }


}


