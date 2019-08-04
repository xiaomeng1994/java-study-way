package java7ConcurrencyCookbook.two;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：2.8在Lock中使用多个条件
 * 替换：[0-9]{1,2}\n
 */

//小工厂
public class Class8 {

    /**
     * 仓库
     */
    static class Warehouse {
        boolean hasProduct = false;//代表仓库有没有产品

        int index = 0;

        ReentrantLock reentrantLock = new ReentrantLock();
        Condition createCondition = reentrantLock.newCondition();//控制生产
        Condition sellCondition = reentrantLock.newCondition();//控制销售

        /**
         * 生产产品
         */
        public void createProduct() throws InterruptedException {
            try {
                reentrantLock.lock();
                while (hasProduct) {
                    System.out.printf("%s:生产时仓库有产品\n", Thread.currentThread().getName());
//                    wait();
                    createCondition.await();
                }
                System.out.printf("%s:生产了一个产品:%s\n", Thread.currentThread().getName(), index);
                hasProduct = true;
//                notify();//通知可以买产品了
                sellCondition.signal();
            } finally {
                reentrantLock.unlock();
            }

        }

        /**
         * 销售产品
         */
        public void sellProduct() throws InterruptedException {
            try {
                reentrantLock.lock();
                while (!hasProduct) {
                    System.out.printf("%s:售卖时仓库没有产品\n", Thread.currentThread().getName());
//                    wait();
                    sellCondition.await();
                }
                System.out.printf("%s:卖出了一个产品:%s\n", Thread.currentThread().getName(), index);
                hasProduct = false;
//                notify();//通知可以买产品了
                createCondition.signal();
                //卖完产品，商品唯一号+1
                index++;
            } finally {
                reentrantLock.unlock();
            }

        }
    }


    /**
     * 生产部门
     */
    static class CreateDepartment implements Runnable {
        Warehouse warehouse;//和仓库关联

        public CreateDepartment(Warehouse warehouse) {
            this.warehouse = warehouse;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    this.warehouse.createProduct();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 销售部门
     */
    static class SellDepartment implements Runnable {
        Warehouse warehouse;//和仓库关联

        public SellDepartment(Warehouse warehouse) {
            this.warehouse = warehouse;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    this.warehouse.sellProduct();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Test
    public void test() {
        Warehouse warehouse = new Warehouse();
        int i = 0;
        for (; i < 50; i++) {
            new Thread(new CreateDepartment(warehouse), String.format("生产部门%s", i)).start();
            new Thread(new SellDepartment(warehouse), String.format("销售部门%s", i)).start();
        }
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}


