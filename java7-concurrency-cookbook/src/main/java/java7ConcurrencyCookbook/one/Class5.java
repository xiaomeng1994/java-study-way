package java7ConcurrencyCookbook.one;

import lombok.AllArgsConstructor;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：1.5操作线程的中断机制
 * 替换：[0-9]{1,2}\n
 */
public class Class5 {

    @AllArgsConstructor
    static class FileSearch extends Thread {

        private String initPath;
        private String fileName;

        /**
         * 为FileSearch类实现run()方法。
         * 它会检测fileName属性是不是路径，如果它是，就调用processDirectory()方法。
         * 这个方法会抛出一个InterruptedException异常，所以我们应该要捕获它。
         */
        @Override
        public void run() {
            File file = new File(initPath);
            if (file.isDirectory()) {
                try {
                    directoryProcess(file);
                } catch (InterruptedException e) {
                    System.out.printf("%s: The search has been interrupted", Thread.currentThread().getName());
                }
            }
        }

        /**
         * 实现 directoryProcess()方法。这个方法会获取文件夹的文件和子文件夹并处理他们。对于每个路径，这个方法会传递路径作为参数来循环调用。
         * 对于每个文件，它会调用fileProcess()方法。处理完全部的文件和文件夹后，它会检查线程有没有被中断，在这个例子，会抛出一个InterruptedException异常。
         *
         * @param file
         * @throws InterruptedException
         */
        private void directoryProcess(File file) throws InterruptedException {
            File list[] = file.listFiles();
            if (list != null) {
                for (int i = 0; i < list.length; i++) {
                    if (list[i].isDirectory()) {
                        directoryProcess(list[i]);
                    } else {
                        fileProcess(list[i]);
                    }
                }
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
        }

        /**
         * 实现 processFile()方法。这方法会比较文件的名字与我们要搜索的文件名。
         * 如果他们一样，就写一条信息到控制台。
         * 比较完后，线程会检查有没有被中断，在这里，它会抛出一个InterruptedException异常。
         *
         * @param file
         * @throws InterruptedException
         */
        private void fileProcess(File file) throws InterruptedException {
            if (file.getName().equals(fileName)) {
                System.out.printf("%s : %s\n", Thread.currentThread().getName(), file.getAbsolutePath());
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
        }
    }


    @Test
    public void test() {
        FileSearch searcher = new FileSearch("D:\\", "autoexec.bat");
        Thread thread = new Thread(searcher);
        thread.setName("child thread");
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }


}


