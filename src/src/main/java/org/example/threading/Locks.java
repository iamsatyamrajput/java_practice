package org.example.threading;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Locks {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bank.withdraw(200);      
            }
        };
        Thread thread1 = new Thread(runnable, "Thread-1 ");
        Thread thread2 = new Thread(runnable, "Thread-2 ");
        
        thread1.start();
        thread2.start();
    }
}

class Bank {
    private int balance = 1000;
    Lock lock = new ReentrantLock();
    public void withdraw(int amount) {
        try {
            if(lock.tryLock(1000, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                System.out.println(Thread.currentThread().getName() + "Lock acquired, processing withdrawal");
                if(balance >= amount) {
                    System.out.println(Thread.currentThread().getName() + "Withdrawing " + amount);
                    balance -= amount;
                    System.out.println(Thread.currentThread().getName() + "Withdrawal successful, new balance: " + balance);
                } else {
                    System.out.println(Thread.currentThread().getName() + "Insufficient funds for withdrawal of " + amount);
                }
            } else {
                System.out.println(Thread.currentThread().getName() + "Could not acquire lock, withdrawal aborted");
                return;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //System.out.println(Thread.currentThread().getName() + " is trying to release the lock");
            lock.unlock();
        }
    }
}
