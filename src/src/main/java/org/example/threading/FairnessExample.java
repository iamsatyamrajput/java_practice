package org.example.threading;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairnessExample {
    public static void main(String[] args) {
        Resource resource = new Resource();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    resource.accessResource();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " was interrupted");
                }
            }
        };
        
        Thread thread1 = new Thread(runnable, "Thread-1");
        Thread thread2 = new Thread(runnable, "Thread-2");
        Thread thread3 = new Thread(runnable, "Thread-3");
        
        thread1.start();
        thread2.start();
        thread3.start();
        
        Resource2 resource2 = new Resource2();
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                try {
                    resource2.accessResource();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " was interrupted");
                }
            }
        };
        
        Thread thread4 = new Thread(runnable2, "Thread-4");
        Thread thread5 = new Thread(runnable2, "Thread-5");
        thread4.start();
        thread5.start();
        thread5.interrupt(); // Interrupting thread5 to demonstrate lockInterruptibly
    }
}

class Resource {
    Lock lock = new ReentrantLock(true); // true for fair locking
    
    public void accessResource() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " running with lock");
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + " encountered an error: " + e.getMessage());
        } finally {
            lock.unlock();
        }
       
    }
}

class Resource2 {
    ReentrantLock lock = new ReentrantLock(true); // true for fair locking

    public void accessResource() {
        try {
            lock.lockInterruptibly();
            Thread.sleep(10000);
            System.out.println(Thread.currentThread().getName() + " running with lock");
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + " encountered an error: " + e.getMessage());
        } finally {
            if(lock.isHeldByCurrentThread()) {
                System.out.println(Thread.currentThread().getName() + " releasing lock");
            } else {
                System.out.println(Thread.currentThread().getName() + " could not release lock as it was not held by this thread");
            }
        }

    }
}
