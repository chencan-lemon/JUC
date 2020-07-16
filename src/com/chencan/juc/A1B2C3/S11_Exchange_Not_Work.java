package com.chencan.juc.A1B2C3;

import java.util.concurrent.Exchanger;

public class S11_Exchange_Not_Work {

    private static Exchanger exchanger = new Exchanger();

    public static void main(String[] args) {

        new Thread(()->{
            for (int i = 1; i < 27; i++) {
                System.out.print(i + " ");
                try {
                    exchanger.exchange("T1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1").start();

        new Thread(()->{
            for (char i = 'A'; i <= 'Z'; i++) {
                try {
                    exchanger.exchange("T2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(i + " ");
            }
        },"t2").start();
    }
}
