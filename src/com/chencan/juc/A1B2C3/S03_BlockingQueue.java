/**
 * BlockingQueue(interface)
 * put:往里装，如果满了的话，我这个线程会阻塞住
 * take:往外取，如果空了的话，我这个线程会阻塞住
 *
 * 我第一个线程打印出了1来了，我就在这边放一个OK,我这边OK了，该你了，
 * 另外一个线程盯着这个事，他take,这个take里面没有值的时候，他是要在
 * 这里阻塞等待的，take不到的时候他就等着，等什么时候第一个线程打印完了，
 * take到了，他就打印这个A，打印完了A之后他就往第二个线程里边放一个OK，
 * 第一个线程也去take第二个线程里面的OK，什么时候take到了，他就接着打印。
 *
 * 总结：
 * 相当于旗帜
 * 我这边OK了，你就可以take到了
 */
package com.chencan.juc.A1B2C3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class S03_BlockingQueue {

    static BlockingQueue  q1 = new ArrayBlockingQueue(1);
    static BlockingQueue  q2 = new ArrayBlockingQueue(1);

    public static void main(String[] args) {
       new Thread(()->{
           for (int i = 1; i < 27; i++) {
               System.out.print(i + " ");
               try {
                   q1.put("ok");//满了，线程阻塞
                   q2.take();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

           }
       },"t1").start();

       new Thread(()->{
           for (char i = 'A'; i <= 'Z'; i++) {
               try {
                   q1.take();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               System.out.print(i + " ");
               try {
                   q2.put("ok");
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       },"t2").start();

    }
}
