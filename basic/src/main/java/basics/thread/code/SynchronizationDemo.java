package basics.thread.code;

public class SynchronizationDemo {

    private int count = 0;               // Instance Variable
    private static int totalCount = 0;   // Static Variable

    private final Object lock = new Object();

    // -------------------------------------------------------
    // 1. No Synchronization (Race Condition)
    // -------------------------------------------------------
    public void incrementWithoutSync() {
        count++;
    }

    // -------------------------------------------------------
    // 2. Synchronized Method (Object Lock)
    // -------------------------------------------------------
    public synchronized void incrementWithSyncMethod() {
        count++;
    }

    // -------------------------------------------------------
    // 3. Synchronized Block (Object Lock)
    // -------------------------------------------------------
    public void incrementWithSyncBlock() {

        synchronized (lock) {
            count++;
        }
    }

    // -------------------------------------------------------
    // 4. Static Synchronization (Class Lock)
    // -------------------------------------------------------
    public static synchronized void incrementStatic() {
        totalCount++;
    }

    // -------------------------------------------------------
    // Main Method
    // -------------------------------------------------------
    public static void main(String[] args) throws Exception {

        SynchronizationDemo demo = new SynchronizationDemo();

        // -------------------------------
        // Race Condition
        // -------------------------------
        Thread t1 = new Thread(() -> {

            for (int i = 0; i < 10000; i++) {
                demo.incrementWithoutSync();
            }

        });

        Thread t2 = new Thread(() -> {

            for (int i = 0; i < 10000; i++) {
                demo.incrementWithoutSync();
            }

        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Race Condition Count : " + demo.count);

        // Reset
        demo.count = 0;

        // -------------------------------
        // Synchronized Method
        // -------------------------------
        Thread t3 = new Thread(() -> {

            for (int i = 0; i < 10000; i++) {
                demo.incrementWithSyncMethod();
            }

        });

        Thread t4 = new Thread(() -> {

            for (int i = 0; i < 10000; i++) {
                demo.incrementWithSyncMethod();
            }

        });

        t3.start();
        t4.start();

        t3.join();
        t4.join();

        System.out.println("Synchronized Method Count : " + demo.count);

        // Reset
        demo.count = 0;

        // -------------------------------
        // Synchronized Block
        // -------------------------------
        Thread t5 = new Thread(() -> {

            for (int i = 0; i < 10000; i++) {
                demo.incrementWithSyncBlock();
            }

        });

        Thread t6 = new Thread(() -> {

            for (int i = 0; i < 10000; i++) {
                demo.incrementWithSyncBlock();
            }

        });

        t5.start();
        t6.start();

        t5.join();
        t6.join();

        System.out.println("Synchronized Block Count : " + demo.count);

        // -------------------------------
        // Static Synchronization
        // -------------------------------
        Thread t7 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                SynchronizationDemo.incrementStatic();
            }

        });

        Thread t8 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                SynchronizationDemo.incrementStatic();
            }

        });

        t7.start();
        t8.start();

        t7.join();
        t8.join();

        System.out.println("Static Count : " + totalCount);
    }
}