/**
 * TransferQueue是一种什么样的队列？
 * 我一个线程往里头生产（生产者线程往里头生产的时侯），
 * 我生产了之后，扔在这里的时候，我这个线程是阻塞的，不动的，
 * 什么时候有另外一个线程把这个拿走了，拿走了之后，这个线程才返回执行
 *
 * 第1个线程上来二话不说先take,相当于第一个线程做了一个消费者，
 * 就在这个Queue等着，看看有没有人往里扔。
 * 第2个线程二话不说上来经过transfer,就把这个字母传进去了，扔进去了一个A，
 * 第1个线程发现很好，来了一个，我就把这个拿出来打印，打印完之后我又进行transfer,
 * 进去了一个1，然后，第2个线程它去里面take,把这个1take出来打印。
 *
 * 相当于我们自己每个人都把自己的一个数字或者是字母交到一个队列里让对方去打印
 */
package com.chencan.juc.A1B2C3;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class S12_TransferQueue {

    public static void main(String[] args) {
        TransferQueue queue = new LinkedTransferQueue();

        new Thread(() -> {
                try {
                    for (int i = 1; i < 27; i++) {
                        System.out.print(queue.take() + " ");
                        queue.transfer(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        }, "t1").start();

        new Thread(() -> {
            try {
                for (char i = 'A'; i <= 'Z'; i++) {
                    queue.transfer(i);
                    System.out.print(queue.take() + " ");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2").start();
    }
}
