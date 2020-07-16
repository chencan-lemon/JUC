/**
 *Condition本质是一个等待队列
 * 更好的原因？
 * 用2个Condition可以精确的指定哪个等待队列里的线程醒过来去执行任务。
 * 借鉴思路：
 * 生产者和消费者线程
 *
 */
package com.chencan.juc.A1B2C3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class S08_lock_condition {

    public static void main(String[] args) {
        Lock lock  = new ReentrantLock();
        Condition c1 = lock.newCondition();
        Condition c2 = lock.newCondition();

        new Thread(()->{
            try {
                lock.lock();
                for (int i = 1; i < 27; i++) {
                    System.out.print(i + " ");
                    c2.signal();//唤醒t2
                    c1.await();//自己（t1）阻塞
                }
                c2.signal();
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
                    c1.signal();
                    c2.await();
                }
                c1.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        },"t2").start();

    }
}
