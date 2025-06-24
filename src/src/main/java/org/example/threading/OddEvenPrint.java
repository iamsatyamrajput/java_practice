package org.example.threading;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OddEvenPrint {
    public static void main(String[] args) throws InterruptedException {
        PrintNumber printNumber = new PrintNumber();
        Runnable oddRunnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i += 1) {
                    printNumber.printCount();
//                    try {
//                       // Thread.sleep(50); // Simulate some processing time
//                    } catch (InterruptedException e) {
//                        System.out.println(Thread.currentThread().getName() + " was interrupted");
//                    }
                }
            }
        };
        Thread thread = new Thread(oddRunnable, "Odd-Thread");
        Thread thread2 = new Thread(oddRunnable, "Even-Thread");
        
        thread.start();
        Thread.sleep(100); // Ensure odd thread starts first
        thread2.start();
        
        thread.join();
        thread2.join();
    }
}
class PrintNumber {
    private int count = 1;
    private final Lock lock = new ReentrantLock(true);

    public void printCount() {
        try {
            lock.lock();
            String threadName = Thread.currentThread().getName();

            if ((count % 2 == 0 && threadName.equals("Even-Thread")) ||
                    (count % 2 != 0 && threadName.equals("Odd-Thread"))) {

                System.out.println(threadName + " is printing count: " + count);
                count++;
            }

        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        return count;
    }
}

