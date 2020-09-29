package com.chencan.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 生产者和消费者模式
 */
public class S01_ArrayBlockingQueue {

    public static void main(String[] args) {

        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(3);
        Interviewer r1 = new Interviewer(queue);
        Consumer c1 = new Consumer(queue);
        new Thread(r1).start();
        new Thread(c1).start();
    }
}

//面试官 生产者
class Interviewer implements Runnable{
    BlockingQueue<String> queue;

    public Interviewer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("10个候选人");
        for (int i = 0; i < 10; i++) {
            String candidate = "Candidate" + i;
            try {
                queue.put(candidate);
                System.out.println("安排好了" + candidate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            queue.put("stop");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

//候选人 消费者
class Consumer implements Runnable{
    BlockingQueue<String> queue;

    public Consumer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);//给每个候选人1秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String msg;
        try {
            while (!(msg = queue.take()).equals("stop")) {
                System.out.println(msg + "到了");
            }
            System.out.println("所有候选人都面试完了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
