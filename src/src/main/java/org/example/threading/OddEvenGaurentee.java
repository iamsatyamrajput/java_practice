package org.example.threading;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OddEvenGaurentee {
    public static void main(String[] args) throws InterruptedException {
        PrintNumberNew printNumber = new PrintNumberNew(100);

        Thread oddThread = new Thread(() -> printNumber.printOdd(), "Odd-Thread");
        Thread evenThread = new Thread(() -> printNumber.printEven(), "Even-Thread");

        oddThread.start();
        evenThread.start();

        oddThread.join();
        evenThread.join();
    }
}

class PrintNumberNew {
    private int count = 1;
    private final int max;
    private final Lock lock = new ReentrantLock(true);
    private final Condition condition = lock.newCondition();
    private boolean isOddTurn = true;

    public PrintNumberNew(int max) {
        this.max = max;
    }

    public void printOdd() {
        while (true) {
            lock.lock();
            try {
                while (!isOddTurn && count <= max) {
                    condition.await();
                }

                if (count > max) {
                    condition.signalAll(); // Wake up even thread if waiting
                    break;
                }

                System.out.println(Thread.currentThread().getName() + " printing: " + count++);
                isOddTurn = false;
                condition.signalAll();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } finally {
                lock.unlock();
            }
        }
    }

    public void printEven() {
        while (true) {
            lock.lock();
            try {
                while (isOddTurn && count <= max) {
                    condition.await();
                }

                if (count > max) {
                    condition.signalAll(); // Wake up odd thread if waiting
                    break;
                }

                System.out.println(Thread.currentThread().getName() + " printing: " + count++);
                isOddTurn = true;
                condition.signalAll();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } finally {
                lock.unlock();
            }
        }
    }
}
