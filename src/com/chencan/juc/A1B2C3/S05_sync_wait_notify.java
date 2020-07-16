/**
 * wait线程阻塞，notify叫醒其它线程，调用这2个方法的时候必须要进行synchronized锁定
 *
 * 第一个线程上来先锁定Object对象o,锁完对象后，我们开始输出第一个数字，输出完之后，
 * notify()唤醒第二个线程，然后自己wait()。
 *
 * 这里面最容易出错的一个地方就是把整个数组都打印完了要记得notify(),
 * 为什么要notify()？
 * 因为这2个线程里面终归有一个线程wait(),阻塞在这停止不动了
 *
 * 如果我想保证t2在t1之前打印，该如何实现？ S06_sync_wait_notify
 */
package com.chencan.juc.A1B2C3;

public class S05_sync_wait_notify {

    public static void main(String[] args) {
        final Object o = new Object();

        new Thread(()->{
            synchronized (o) {
                for (int i = 1; i < 27; i++) {
                    System.out.print(i + " ");
                    try {
                        o.notify();//唤醒第二个线程
                        o.wait();//自己wait()
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();//必须，否则无法停止程序
            }

        },"t1").start();

        new Thread(()->{
            synchronized (o) {
                for (char i = 'A'; i <= 'Z'; i++) {
                    System.out.print(i + " ");
                    try {
                        o.notify();
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        },"t2").start();
    }
}
