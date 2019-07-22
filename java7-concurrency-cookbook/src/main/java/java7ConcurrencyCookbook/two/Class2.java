package java7ConcurrencyCookbook.two;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

/**
 * 课程：java并发编程7学习笔记(http://ifeve.com/java-7-concurrency-cookbook/)
 * 章节：2.2同步方法
 * 替换：[0-9]{1,2}\n
 */
public class Class2 {

    @Setter
    @Getter
    class Account {

        private volatile double balance;

        public /*synchronized*/ void addAmount(double amount) {
            double tmp = balance;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tmp += amount;
            balance = tmp;
        }

        public /*synchronized*/ void subtractAmount(double amount) {
            double tmp = balance;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tmp -= amount;
            balance = tmp;
        }
    }

    class Bank implements Runnable {
        private Account account;

        public Bank(Account account) {
            this.account = account;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                account.subtractAmount(1000);
            }
        }

    }

    class Company implements Runnable {
        private Account account;

        public Company(Account account) {
            this.account = account;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                account.addAmount(1000);
            }
        }
    }

    @Test
    public void test() {
        Account account = new Account();
        account.setBalance(1000);
        Company company = new Company(account);
        Thread companyThread = new Thread(company);
        Bank bank = new Bank(account);
        Thread bankThread = new Thread(bank);
        System.out.printf("Account : Initial Balance: %f\n", account.getBalance());
        companyThread.start();
        bankThread.start();
        try {
            companyThread.join();
            bankThread.join();
            System.out.printf("Account : Final Balance: %f\n", account.getBalance());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}


