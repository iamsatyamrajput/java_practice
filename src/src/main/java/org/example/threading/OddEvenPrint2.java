package org.example.threading;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class OddEvenPrint2 {
    public static void main(String[] args) {
        OddEven oddEvenPrint2 = new OddEven();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                oddEvenPrint2.printEven();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                oddEvenPrint2.printOdd();
            }
        });
        t1.start();
        t2.start();
    }
}

class OddEven {
    ReentrantLock lock = new ReentrantLock();
    boolean isEven = true;
    Condition condition = lock.newCondition();
    int number = 0;
    int max = 100;
    public void printOdd() {
        lock.lock();
        try {
            while(isEven) {
                condition.await();
            }
            System.out.println(Thread.currentThread().getName() + " " + number);
            number++;
            isEven = !isEven;
            condition.signal();
            if(number > max) {
                Thread.currentThread().interrupt();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    
    public void printEven() {
        lock.lock();
        try {
            while(!isEven) {
                condition.await();
            }
            System.out.println(Thread.currentThread().getName() + " " + number);
            number++;
            isEven = !isEven;
            
            condition.signal();
            if(number > max) {
                Thread.currentThread().interrupt();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

    }
}
