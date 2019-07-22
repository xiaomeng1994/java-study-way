package java7ConcurrencyCookbook.one;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：1.8守护线程的创建和运行
 * 替换：[0-9]{1,2}\n
 */
public class Class8 {

    @Setter
    @Getter
    static class Event {
        private String event;
        private Date date;
    }

    @AllArgsConstructor
    static class WriterTask implements Runnable {

        private Deque<Event> deque;

        @Override
        public void run() {
            for (int i = 1; i < 100; i++) {
                Event event = new Event();
                event.setDate(new Date());
                event.setEvent(String.format("The thread %s has generated an event", Thread.currentThread().getId()));
                deque.addFirst(event);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class CleanerTask extends Thread {

        private Deque<Event> deque;

        public CleanerTask(Deque<Event> deque) {
            this.deque = deque;
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                Date date = new Date();
                clean(date);
            }
        }

        private void clean(Date date) {
            long difference;
            boolean delete;
            if (deque.size() == 0) {
                return;
            }
            delete = false;
            do {
                Event e = deque.getLast();
                difference = date.getTime() - e.getDate().getTime();
                if (difference > 10000) {
                    System.out.printf("Cleaner: %s\n", e.getEvent());
                    deque.removeLast();
                    delete = true;
                }
            } while (difference > 10000);
            if (delete) {
                System.out.printf("Cleaner: Size of the queue: %d\n", deque.size());
            }
        }

    }


    @Test
    public void test() {
        //Deque双向队列
        Deque<Event> deque = new ArrayDeque<>();
        WriterTask writer = new WriterTask(deque);
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(writer);
            thread.start();
        }
        CleanerTask cleaner = new CleanerTask(deque);
        cleaner.start();
        try {
            //使主线程进入等待完成
            cleaner.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}


