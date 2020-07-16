/**
 * 实现一个容器，提供两个方法add、size，写两个线程：
 * 线程1，添加10个元素到容器中
 * 线程2，实时监控元素个数，当个数到5个时，线程2给出提示并结束
 */
/**
 * 注释掉t1线程里的TimeUnit.SECONDS.sleep(1);程序还是无法运行成功
 * volatile修饰List集合，虽然实现了线程间信息的传递，但还是与不足之处，
 * volatile一定要尽量去修饰普通的值，不要去修饰引用值，
 * 因为volatile修饰引用类型，这个引用对象指向的是另外一个new出来的对象，
 * 如果这个对象里边的成员变量的值改变了，是无法观察到的，所以小程序2也不理想
 */
package com.chencan.juc.addandsize;

import java.util.ArrayList;
import java.util.List;

public class S02_WithVolatile {

    volatile List list = new ArrayList<>();

    public void add(Object o) {
        list.add(o);
    }

    public int getSize() {
        return list.size();
    }

    public static void main(String[] args) {
        S02_WithVolatile c = new S02_WithVolatile();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                Object o = new Object();
                c.add(o);
                System.out.println("add " + i);
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        },"t1").start();

        new Thread(()->{
            while (true) {
                if (c.getSize() == 5) {
                    break;
                }
            }
            System.out.println("t2 结束");
        },"t2").start();

    }

}
