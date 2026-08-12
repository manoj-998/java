package basics.thread.code;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ThreadCreationDemo {

    public static void main(String[] args) throws Exception {

        // 1. Extending Thread
        Thread thread1 = new MyThread();
        thread1.start();

        // 2. Implementing Runnable
        Thread thread2 = new Thread(new MyRunnable());
        thread2.start();


        // 3. Callable + FutureTask
        Callable<Integer> callable = new MyCallable();
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        Thread thread3 = new Thread(futureTask);
        thread3.start();
        Integer result = futureTask.get();
        System.out.println("Callable Result: " + result);

        // 4. Lambda Expression
        Thread thread4 = new Thread(() -> {
            System.out.println("Lambda running: " + Thread.currentThread().getName());
        });

        thread4.start();
    }
}

class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println("Extending Thread running: " + Thread.currentThread().getName());
    }
}

class MyRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("Runnable running: " + Thread.currentThread().getName());
    }
}

class MyCallable implements Callable<Integer> {

    @Override
    public Integer call() {
        System.out.println("Callable running: " + Thread.currentThread().getName());
        return 100;
    }
}