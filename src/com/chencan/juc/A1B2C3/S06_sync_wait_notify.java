/**
 * 保证t2在t1之前打印
 */
package com.chencan.juc.A1B2C3;

public class S06_sync_wait_notify {

    private static volatile boolean t2Started = false;//volatile 保证线程可见性


    public static void main(String[] args) {
        final Object o = new Object();

        new Thread(()->{
            synchronized (o) {
                while (!t2Started) {
                    try {
                        o.wait();//自己阻塞
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                for (int i = 1; i < 27; i++) {
                    System.out.print(i + " ");
                    try {
                        o.notify();//唤醒第二个线程
                        o.wait();//自己阻塞
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        },"t1").start();

        new Thread(()->{
            synchronized (o) {
                for (char i = 'A'; i <= 'Z'; i++) {
                    System.out.print(i + " ");
                    t2Started = true;
                    try {
                        o.notify();//唤醒第一个线程
                        o.wait();//自己阻塞
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        },"t2").start();
    }
}
