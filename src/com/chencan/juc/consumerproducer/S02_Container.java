/**
 * 写一个固定容量同步容器，拥有put和get方法，以及getCount方法，
 * 能够支持2个生产者线程以及10个消费者线程的阻塞调用
 */
/**
 * ReentrantLock替代synchronized?
 * 原来写synchronized的地方换写lock.lock,加完锁之后需要注意的是记得lock.unlock()解锁，
 * 由于synchronized是自动解锁的，大括号执行完就结束了。lock就不行了，lock必须的手动解锁，
 * 手动解锁一定要写在try...finally里面保证一定要解锁，不然的话上锁之后中间执行的过程有问题了，
 * 死在那了，别人就永远也拿不到这把锁了。
 */
/**
 * 生产者线程只唤醒消费者线程，消费者线程只负责唤醒生产者线程。
 * 1.ReentrantLock,能够精确的指定哪些线程被唤醒，注意是哪些不是哪个
 * 2.Lock和Condition的本质
 *  Lock的本质是在synchronized里调用wait()和notify()的时候，只有一个等待队列
 *  Condition的本质就是等待队列个数。以前只有一个等待队列，现在我new了2个Condition,
 *  一个叫producer一个等待队列出来了，另一个叫consumer第二个等待队列出来了
 *3.当我们使用producer.await();的时候，指的是当前线程进入producer的等待队列，
 * 使用producer.signalAll()指的是唤醒producer这个等待队列的线程，consumer也是如此
 */

package com.chencan.juc.consumerproducer;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class S02_Container<T> {

    final private LinkedList<T> list = new LinkedList<>();
    final private int MAX = 10;
    private int count = 0;

    private Lock lock = new ReentrantLock();
    private Condition producer = lock.newCondition();
    private Condition consumer = lock.newCondition();

    //消费者
    public  T get() {
        T t = null;
        try {
            lock.lock();
            while (list.size() == 0) {
               consumer.await();
            }
            t = list.removeFirst();
            --count;
            //在消费者线程里唤醒producer等待队列的线程
            producer.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return t;
    }

    //生产者
    public void put(T t) {
        try {
            lock.lock();
            while (list.size() == MAX) {
                producer.await();
            }
            list.add(t);
            ++count;
            //在生产者线程里唤醒consumer等待队列的线程
            consumer.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        S02_Container<String> c = new S02_Container<>();
        //启动消费者线程
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 5; j++)
                    System.out.println(c.get());
            },"c" + i).start();
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //启动生产者线程
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                for (int j = 0; j < 25; j++)
                    c.put(Thread.currentThread().getName() + " " + j);
            },"p" + i).start();
        }
    }

}
