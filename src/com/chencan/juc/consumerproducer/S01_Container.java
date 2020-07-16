/**
 * 写一个固定容量同步容器，拥有put和get方法，以及getCount方法，
 * 能够支持2个生产者线程以及10个消费者线程的阻塞调用
 */
/**
 * 1.加synchronized，因为++count我们生产了3个馒头，当还没来得及加的时候，count值为2的时候，
 * 另外一个线程读到的值很可能是2，并不是3，所以不加锁会出问题。
 * （用了锁以后就只有一个线程在运行）
 *
 * 2.为什么用while不用if?
 * 因为当LinedList集合中馒头数等于最大值的时候，if在判断了集合的大小等于MAX的时候，调用了wait()方法以后，不会再去判断一次，
 * 方法会继续往下运行，假如在你wait()以后，另外一个方法又添加了一个馒头，你没有再次判断，就又添加了一次，造成数据错误，就会
 * 出现问题，因此必须用while
 *
 * 3.notifyAll()唤醒线程
 * notifyAll()方法会唤醒等待队列的所有线程。比如我们是生产者线程，生产满了，满了以后我们唤醒消费者线程，可是很不幸的是，它
 * 同样的也会唤醒另外一个生产者线程，而我们只想唤醒消费者线程。
 * 我们能不能做到生产者线程只唤醒消费者线程，消费者线程只负责唤醒生产者线程？S02_Container
 */
package com.chencan.juc.consumerproducer;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class S01_Container<T> {

    final private LinkedList<T> list = new LinkedList<>();
    final private int MAX = 10;
    private int count = 0;

    //消费者
    public synchronized T get() {
        T t = null;
        while (list.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        t = list.removeFirst();
        --count;
        //通知生产者
        this.notifyAll();
        return t;
    }

    //生产者
    public synchronized void put(T t) {
        while (list.size() == MAX) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        list.add(t);
        ++count;
        //通知消费者
        this.notifyAll();
    }

    public static void main(String[] args) {
        S01_Container<String> c = new S01_Container<>();

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
