package org.example.threading;

public class ThreadTest {
    
    public void test() {
        MyThread myThread = new MyThread();
        myThread.start();
        Thread myRunnableThread = new Thread(new MyRunnable());
        myRunnableThread.start();
        
    }
    
    class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is running");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread finished");
        }
    }
    class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is running");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Runnable finished");
        }
    }

    public static void main(String[] args) {
        ThreadTest threadTest = new ThreadTest();
        threadTest.test();
    }
}
