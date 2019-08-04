package java7ConcurrencyCookbook.three;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;



/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：2.7使用读/写锁同步数据访问
 * 替换：[0-9]{1,2}\n
 */
@Slf4j
public class class1 {

    //1.创建一个会实现print queue的类名为 PrintQueue。
    class PrintQueue {
        //2.声明一个对象为Semaphore，称它为semaphore。
        private final Semaphore semaphore;

        //3.实现类的构造函数并初始能保护print quere的访问的semaphore对象的值。
        public PrintQueue() {
            semaphore = new Semaphore(1);
        }

        //4.实现Implement the printJob()方法，此方法可以模拟打印文档，并接收document对象作为参数。
        public void printJob(Object document) {
            //5.在这方法内，首先，你必须调用acquire()方法获得demaphore。这个方法会抛出 InterruptedException异常，使用必须包含处理这个异常的代码。
            try {
                log.info("{}:is waiting", Thread.currentThread().getName());
                semaphore.acquire();
                //6.然后，实现能随机等待一段时间的模拟打印文档的行。
                long duration = (long) (Math.random() * 10);
                log.info("{}: PrintQueue: Printing a Job during {} seconds", Thread.currentThread().getName(), duration);
                //Thread.sleep(duration);
                TimeUnit.SECONDS.sleep(duration);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //7.最后，释放semaphore通过调用semaphore的relaser()方法。
                semaphore.release();
            }
        }
    }


    //8.创建一个名为Job的类并一定实现Runnable 接口。这个类实现把文档传送到打印机的任务。
    class Job implements Runnable {

        //9.声明一个对象为PrintQueue，名为printQueue。
        private PrintQueue printQueue;

        //10.实现类的构造函数，初始化这个类里的PrintQueue对象。
        public Job(PrintQueue printQueue) {
            this.printQueue = printQueue;
        }

        //11.实现方法run()。
        @Override
        public void run() {
            //12.首先， 此方法写信息到操控台表明任务已经开始执行了。
            log.info("{}: Going to print a job", Thread.currentThread().getName());
            //13.然后，调用PrintQueue 对象的printJob()方法。
            printQueue.printJob(new Object());
            //14.最后， 此方法写信息到操控台表明它已经结束运行了。
            log.info("{}: The document has been printed", Thread.currentThread().getName());
        }

    }

    //15.实现例子的main类，创建名为 Main的类并实现main()方法。
    @Test
    public void test() {
        //16.创建PrintQueue对象名为printQueue。
        PrintQueue printQueue = new PrintQueue();

        //17.创建10个threads。每个线程会执行一个发送文档到print queue的Job对象，并开始这10个线程们。
        Thread thread[] = new Thread[10];
        for (int i = 0; i < 10; i++) {
            thread[i] = new Thread(new Job(printQueue), "Thread" + i);
            thread[i].start();
        }

        try {
            //18.最后，等待线程完成
            for (int i = 0; i < 10; i++) {
                thread[i].join();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
