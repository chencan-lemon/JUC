/**
 * LockSupport
 * park 当前线程阻塞
 * unpark(Thread t) 唤醒其它线程
 */
package com.chencan.juc.A1B2C3;

import java.util.concurrent.locks.LockSupport;


public class S01_LockSupport {

    static Thread t1 = null, t2 = null;
    public static void main(String[] args) {
        t1 = new Thread(()->{
            for (int i = 1; i < 27; i++) {
                System.out.print(i + " ");
                LockSupport.unpark(t2);//唤醒t2
                LockSupport.park();//t1阻塞
            }
        },"t1");

        t2 = new Thread(()->{
            for (char i = 'A'; i <= 'Z'; i++) {
                LockSupport.park();//t2阻塞
                System.out.print(i + " ");
                LockSupport.unpark(t1);//唤醒t1
            }
        },"t2");

        t1.start();
        t2.start();
    }
}
