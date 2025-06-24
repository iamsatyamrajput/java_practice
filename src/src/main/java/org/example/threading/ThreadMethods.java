package org.example.threading;


public class ThreadMethods {
    public void test() {
       MyThread myThread = new MyThread("t1");
    }

    public static void main(String[] args) {
        ThreadMethods tm = new ThreadMethods();
        tm.test();
    }
}
class MyThread extends Thread {
    
    public MyThread(String name) {
        super(name);
        
    }
    @Override
    public void run() {
        try {
            for (int i =0; i < 5; i++) {
                Thread.yield(); // Yielding the thread to allow other threads to run
                System.out.println(Thread.currentThread().getName() + " is running");
                Thread.sleep(100); 
            }
            
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
