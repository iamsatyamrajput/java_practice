package org.example.threading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Work work = new Work();
        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Callable<Long> runnable = work::doWork;
            futures.add(executorService.submit(runnable));
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(futures.get(i).get());
        }
    }
}

class Work {
    public long doWork() {
        
        System.out.println("Doing work in " + Thread.currentThread().getName());
        return System.currentTimeMillis();
                
    }
}


