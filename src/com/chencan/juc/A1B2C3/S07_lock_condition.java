/**
 * 基于CAS操作新类型的锁 ReentrantLock
 *
 * ReentrantLock的写法就是synchronize的一个变种
 *
 * ReentrantLock替代synchronized?
 * 原来写synchronized的地方换写lock.lock,加完锁之后需要注意的是记得lock.unlock()解锁，
 * 由于synchronized是自动解锁的，大括号执行完就结束了。lock就不行了，lock必须的手动解锁，
 * 手动解锁一定要写在try...finally里面保证一定要解锁，不然的话上锁之后中间执行的过程有问题了，
 * 死在那了，别人就永远也拿不到这把锁了。
 *
 * signal()相当于notify() 唤醒其它线程
 * await()相当于wait() 线程阻塞
 *
 * S08_lock_condition更好
 */
package com.chencan.juc.A1B2C3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class S07_lock_condition {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(()->{
            try {
                lock.lock();
                for (int i = 1; i < 27; i++) {
                    System.out.print(i + " ");
                    condition.signal();//唤醒t2线程
                    condition.await();//自己阻塞
                }
                condition.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        },"t1").start();

        new Thread(()->{
            try {
                lock.lock();
                for (char i = 'A'; i <= 'Z'; i++) {
                    System.out.print(i + " ");
                    condition.signal();
                    condition.await();
                }
                condition.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        },"t2").start();
    }
}
