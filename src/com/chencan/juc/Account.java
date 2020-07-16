/**
 * 模拟银行账户
 * 对业务写方法加锁
 * 对业务读方法不加锁
 * 这样行不行？
 *
 * 容易产生脏读问题（dirtyRead）
 */
/**
 *问题的产生就是synchronized方法和非synchronized方法是同时运行的。
 * 解决就是把getBalance加上synchronized就可以了，如果业务允许脏读，就可以不用加锁，
 * 加锁之后的效率低下。
 */
package com.chencan.juc;

import java.util.concurrent.TimeUnit;

public class Account {
    String name;
    double balance;

    public synchronized void setBalance(String name, double balance) {
        this.name = name;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.balance = balance;
    }

    public synchronized double getBalance(String name) {
        return this.balance;
    }

    public static void main(String[] args) {
        Account a = new Account();
        new Thread(()->a.setBalance("xiaoming",100.0)).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(a.getBalance("xiaoming"));

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(a.getBalance("xiaoming"));
    }
}
