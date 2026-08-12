package basics.thread.code;

public class ThreadMethodsDemo {

    public static void main(String[] args) throws Exception {

        Thread t1 = new Thread(() -> {

            System.out.println("1. Current Thread : " + Thread.currentThread().getName());

            try {

                // sleep()
                System.out.println("2. Sleeping for 2 seconds...");
                Thread.sleep(2000);

                // yield()
                System.out.println("3. Calling yield()...");
                Thread.yield();

                for (int i = 1; i <= 5; i++) {

                    // interrupt()
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("4. Thread Interrupted!");
                        return;
                    }

                    System.out.println("Working : " + i);
                    Thread.sleep(500);
                }

            } catch (InterruptedException e) {
                System.out.println("Interrupted while sleeping");
            }

            System.out.println("Thread Completed");

        }, "Worker-Thread");



        // setPriority()
        t1.setPriority(Thread.MAX_PRIORITY);

        // getPriority()
        System.out.println("Priority : " + t1.getPriority());

        // isAlive()
        System.out.println("Before start() : " + t1.isAlive());

        // start()
        t1.start();

        // isAlive()
        System.out.println("After start() : " + t1.isAlive());

        Thread.sleep(1000);

        // interrupt()
        System.out.println("Main thread interrupting Worker...");
        t1.interrupt();

        // join()
        t1.join();

        System.out.println("After join() : Worker Finished");

        // isAlive()
        System.out.println("After completion : " + t1.isAlive());

    }
}