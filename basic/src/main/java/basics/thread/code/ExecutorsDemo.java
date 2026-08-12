package basics.thread.code;

import java.util.concurrent.*;

public class ExecutorsDemo {

    public static void main(String[] args) throws Exception {

        // ====================================================
        // 1. Single Thread Executor
        // ====================================================
        System.out.println("\n===== SingleThreadExecutor =====");
        ExecutorService single = Executors.newSingleThreadExecutor();
        single.submit(() ->
                System.out.println(Thread.currentThread().getName() + " -> Task 1"));
        single.submit(() ->
                System.out.println(Thread.currentThread().getName() + " -> Task 2"));
        single.shutdown();


        // ====================================================
        // 2. Fixed Thread Pool
        // ====================================================
        System.out.println("\n===== FixedThreadPool =====");
        ExecutorService fixed = Executors.newFixedThreadPool(2);
        for (int i = 1; i <= 5; i++) {
            int task = i;
            fixed.submit(() ->
                    System.out.println(Thread.currentThread().getName()
                            + " -> Task " + task));
        }
        fixed.shutdown();


        // ====================================================
        // 3. Cached Thread Pool
        // ====================================================
        System.out.println("\n===== CachedThreadPool =====");
        ExecutorService cached = Executors.newCachedThreadPool();
        for (int i = 1; i <= 5; i++) {
            int task = i;
            cached.submit(() ->
                    System.out.println(Thread.currentThread().getName()
                            + " -> Task " + task));
        }
        cached.shutdown();

        // ====================================================
        // 4. Scheduled Executor
        // ====================================================
        System.out.println("\n===== ScheduledExecutor =====");
        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(2);
        scheduler.schedule(() ->
                        System.out.println("Executed after 3 seconds"),
                3,
                TimeUnit.SECONDS);
        scheduler.shutdown();


        // ====================================================
        // 5. Callable + Future
        // ====================================================
        System.out.println("\n===== Callable + Future =====");

        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(() -> {
            Thread.sleep(2000);
            return 100;
        });

        System.out.println("Waiting for Result...");
        Integer result = future.get();
        System.out.println("Result = " + result);
        service.shutdown();
        
        // ====================================================
        // 6. ThreadPoolExecutor
        // ====================================================
        System.out.println("\n===== ThreadPoolExecutor =====");
        ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        executor.submit(() ->
                System.out.println("Pool Size : " + executor.getPoolSize()));
        executor.shutdown();
    }
}