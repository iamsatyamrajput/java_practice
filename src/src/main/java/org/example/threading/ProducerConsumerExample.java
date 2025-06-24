package org.example.threading;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerExample {
    public static void main(String[] args) throws InterruptedException {
        DataQueue<String> queue = new DataQueue<>();

        int numProducers = 1;
        int numConsumers = 3;

        for (int i = 0; i < numProducers; i++) {
            new Thread(new Producer(queue), "Producer-" + i).start();
        }

        for (int i = 0; i < numConsumers; i++) {
            new Thread(new Consumer(queue), "Consumer-" + i).start();
        }
        
    }
}
class Producer implements Runnable {
    private final DataQueue queue;
    
    public Producer(DataQueue queue) {
        this.queue = queue;
    }
    
    public void run() {
        for (int i = 0; i < 60; i++) {
            //System.out.println(Thread.currentThread().getName() + "Produced"+ "data" + i);
            queue.produce( "data" + i);
        }
    }
}

class Consumer implements Runnable {
    private final DataQueue queue;
    
    public Consumer(DataQueue queue) {
        this.queue = queue;
    }
    
    public void run() {
        for (int i = 0; i < 10; i++) {
            String data = (String) queue.consume();
            //System.out.println(Thread.currentThread().getName() + " Consumed: " + data);
        }
    }
}

class DataQueue<D> {
    private final Queue<D> queue = new LinkedList<>();
    private final int MAX_SIZE = 10;
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition(); // FIXED

    public void produce(D data) {
        lock.lock();
        try {
            while (queue.size() >= MAX_SIZE) {
                notFull.await(); // FIXED
            }
            queue.add(data);
            System.out.println(Thread.currentThread().getName() + " produced: " + data);
            notEmpty.signal(); // FIXED: Notify one waiting consumer
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Producer interrupted", e);
        } finally {
            lock.unlock();
        }
    }

    public D consume() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await(); // FIXED
            }
            D data = queue.poll();
            System.out.println(Thread.currentThread().getName() + " consumed: " + data);
            notFull.signal(); // FIXED: Notify one waiting producer
            return data;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Consumer interrupted", e);
        } finally {
            lock.unlock();
        }
    }
}



