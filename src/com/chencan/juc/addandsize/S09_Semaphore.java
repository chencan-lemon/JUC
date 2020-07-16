/**
 * 实现一个容器，提供两个方法add、size，写两个线程：
 * 线程1，添加10个元素到容器中
 * 线程2，实时监控元素个数，当个数到5个时，线程2给出提示并结束
 */
/**
 * 按预期执行的原因
 * 创建一个Semaphore对象，设置只能有1一个线程可以运行，
 * t1线程开始启动，调用acquire()方法限制其它线程运行，在for循环
 * 添加了5个对象以后，调用s.release()表示其它线程可以运行，这个时候
 * t1线程启动t2线程，调用join()把CPU的控制权交给t2线程，t2线程打印出相应提示，t2线程执行结束
 * t1线程调用acquire()方法重新获得执行权，继续输出后来的对象添加信息
 */
package com.chencan.juc.addandsize;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class S09_Semaphore {
    //添加volatile,使t2能够得到通知
    volatile List list = new ArrayList<>();

    public void add(Object o) {
        list.add(o);
    }

    public int getSize() {
        return list.size();
    }
    
    static Thread t1 = null, t2 = null;
    
    public static void main(String[] args) {
        S09_Semaphore c = new S09_Semaphore();
        Semaphore s = new Semaphore(1);

        t2 = new Thread(()->{
            System.out.println("t2 启动");
            try {
                s.acquire();
                System.out.println("t2 结束");
                s.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2");
        
        t1 = new Thread(()->{
            System.out.println("t1 启动");
            try {
                //t1线程获得执行权
                s.acquire();
                for (int i = 0; i < 5; i++) {
                    c.add(new Object());
                    System.out.println("add " + i);
                }
                //释放锁
                s.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                t2.start();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                s.acquire();
                for (int i = 5; i < 10; i++) {
                    System.out.println("add " + i);
                }
                s.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1");
        t1.start();
    }
}
