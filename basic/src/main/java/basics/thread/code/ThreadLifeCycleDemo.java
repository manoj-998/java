package basics.thread.code;

public class ThreadLifeCycleDemo {

    public static void main(String[] args) throws Exception {

        Object lock = new Object();

        Thread t1 = new Thread(() -> {

            try {
                System.out.println("[T1] RUNNING");

                // TIMED_WAITING
                System.out.println("[T1] Sleeping for 3 seconds...");
                Thread.sleep(3000);

                synchronized (lock) {

                    System.out.println("[T1] Acquired lock");

                    // WAITING
                    System.out.println("[T1] Calling wait()...");
                    lock.wait();

                    System.out.println("[T1] Resumed after notify()");
                }

                System.out.println("[T1] Execution Completed");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "Thread-1");



        Thread t2 = new Thread(() -> {

            try {

                Thread.sleep(3500);

                synchronized (lock) {

                    System.out.println("[T2] Acquired lock");

                    System.out.println("[T2] Sleeping for 3 seconds while holding lock...");
                    Thread.sleep(3000);

                    System.out.println("[T2] Calling notify()");
                    lock.notify();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, "Thread-2");


        // NEW
        System.out.println("1. State after creation : " + t1.getState());

        t1.start();

        Thread.sleep(500);

        // RUNNABLE / RUNNING
        System.out.println("2. State after start() : " + t1.getState());

        t2.start();

        Thread.sleep(3200);

        // BLOCKED (waiting for lock held by T2)
        System.out.println("3. State while waiting for lock : " + t1.getState());

        Thread.sleep(3500);

        // WAITING
        System.out.println("4. State after wait() : " + t1.getState());

        Thread.sleep(2000);

        // RUNNABLE
        System.out.println("5. State after notify() : " + t1.getState());

        t1.join();
        t2.join();

        // TERMINATED
        System.out.println("6. Final State : " + t1.getState());
    }
}
