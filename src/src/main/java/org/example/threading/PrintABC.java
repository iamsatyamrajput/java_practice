package org.example.threading;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class PrintABC {
    public static void main(String[] args) {
       AtomicInteger atomicInteger = new AtomicInteger();
       atomicInteger.set(0);
       PrintAlphabet printAlphabet = new PrintAlphabet();
        Thread t1 = new Thread(() -> {
            while (atomicInteger.get() < 26) {
                printAlphabet.printABC(atomicInteger.getAndIncrement());
            }
        });
        Thread t2 = new Thread(() -> {
            while (atomicInteger.get() < 26) {
                printAlphabet.printABC(atomicInteger.getAndIncrement());
            }
        });
        Thread t3 = new Thread(() -> {
            while (atomicInteger.get() < 26) {
                printAlphabet.printABC(atomicInteger.getAndIncrement());
            }
        });
        t1.start();
        t2.start();
        t3.start();
        
    }
}

class PrintAlphabet {
    ReentrantLock lock = new ReentrantLock(true);
    public void printABC(int index) {
        lock.lock();    
        try {
            char c = (char) ('A' + index);
            System.out.println(Thread.currentThread().getName()+" " + c);
        } catch (Exception e) {
                
        } finally {
            lock.unlock();
        }
    }
}
