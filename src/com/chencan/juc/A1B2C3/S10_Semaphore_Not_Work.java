package com.chencan.juc.A1B2C3;

import java.util.concurrent.Semaphore;

public class S10_Semaphore_Not_Work {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1, true);

        new Thread(()->{
            for (int i = 1; i < 27; i++) {
                System.out.print(i + " ");
                semaphore.release();
            }
        },"t1").start();

        new Thread(()->{
            for (char i = 'A'; i <= 'Z'; i++) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(i + " ");
                semaphore.release();
            }
        },"t2").start();
    }
}
