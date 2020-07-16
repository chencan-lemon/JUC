/**
 * 与S02_cas写法类似
 */
package com.chencan.juc.A1B2C3;

import java.util.concurrent.atomic.AtomicInteger;

public class S04_AtomicInteger {

    static AtomicInteger ai = new AtomicInteger(1);

    public static void main(String[] args) {

        new Thread(()->{
            for (int i = 1; i < 27; i++) {
                while (ai.get() != 1) {};
                System.out.print(i + " ");
                ai.set(2);
            }
        },"t1").start();

        new Thread(()->{
            for (char i = 'A'; i <= 'Z'; i++) {
                while (ai.get() != 2) {};
                System.out.print(i + " ");
                ai.set(1);
            }
        },"t2").start();
    }

}
