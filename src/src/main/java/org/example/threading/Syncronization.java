package org.example.threading;

public class Syncronization {

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        IncrementThread incrementThread = new IncrementThread(counter);
        IncrementThread incrementThread2 = new IncrementThread(counter);

        incrementThread.start();
        incrementThread2.start();
        
        incrementThread.join();
        incrementThread2.join();
        
        System.out.println("Final count: " + counter.getCount());
    }
}
class IncrementThread extends Thread {
    private final Counter counter;

    public IncrementThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            counter.increment();
        }


    }
}
class Counter{
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    } 
    }
