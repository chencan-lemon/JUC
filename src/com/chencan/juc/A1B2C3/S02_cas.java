package com.chencan.juc.A1B2C3;


public class S02_cas {

    enum ReadyToRun{T1, T2}//enum类型：防止取别的值

    static volatile ReadyToRun r = ReadyToRun.T1;//思考为什么必须volatile？ 保证线程可见性

    public static void main(String[] args) {

        new Thread(()->{
            for (int i = 1; i < 27; i++) {
                while (r != ReadyToRun.T1){}
                System.out.print(i + " ");
                r = ReadyToRun.T2;

            }
        },"t1").start();

        new Thread(()->{
            for (char i = 'A'; i <= 'Z'; i++) {
                while (r != ReadyToRun.T2){}
                System.out.print(i + " ");
                r = ReadyToRun.T1;
            }
        },"t2").start();
    }
}
