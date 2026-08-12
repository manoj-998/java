//package basics.feature;
//
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.StructuredTaskScope;
//
//
//public class Java19FeaturesDemo {
//
//    public static void main(String[] args)
//            throws InterruptedException, ExecutionException {
//
//        demonstrateVirtualThread();
//
//        demonstrateRecordPattern();
//        demonstratePatternMatchingSwitch();
//
//        /**
//         * - **Structured Concurrency(perview)** is a Java 21 feature that groups multiple concurrent tasks into a single unit of work.
//         * - If one task fails, the remaining tasks can be cancelled automatically, making concurrent code easier to manage and safer.
//         */
//        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
//            System.out.println("Structured Concurrency");
//            // Task 1
//            var userTask = scope.fork(() -> {
//                Thread.sleep(1000);
//                return "User Details";
//            });
//
//            // Task 2
//            var orderTask = scope.fork(() -> {
//                Thread.sleep(2000);
//                return "Order Details";
//            });
//
//            // Wait for all tasks
//            scope.join();
//
//            // Throw exception if any task failed
//            scope.throwIfFailed();
//            System.out.println(userTask.get());
//            System.out.println(orderTask.get());
//        }
//    }
//
//
//    /**
//     * 1. Virtual Thread
//     *
//     * Virtual threads are lightweight threads managed by the JVM.
//     * They are useful for applications that perform many blocking I/O tasks.
//     */
//    private static void demonstrateVirtualThread() throws InterruptedException {
//
//        System.out.println("\n===== Virtual Thread =====");
//        Thread virtualThread = Thread.startVirtualThread(() -> {
//            System.out.println("Running thread : "                     + Thread.currentThread());
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException exception) {
//                Thread.currentThread().interrupt();
//            }
//            System.out.println("Virtual-thread task completed");
//        });
//        virtualThread.join();
//    }
//
//    private static String fetchCustomer() throws InterruptedException {
//
//        Thread.sleep(500);
//
//        System.out.println(
//                "Customer task : "
//                        + Thread.currentThread());
//
//        return "Manoj";
//    }
//
//    private static Double fetchBalance()
//            throws InterruptedException {
//
//        Thread.sleep(700);
//
//        System.out.println(
//                "Balance task  : "
//                        + Thread.currentThread());
//
//        return 85_000.00;
//    }
//
//    /**
//     * 3. Record Pattern
//     *
//     * A record pattern extracts record component values directly.
//     * It removes the need to call each accessor method manually.
//     */
//    private static void demonstrateRecordPattern() {
//
//        System.out.println("\n===== Record Pattern =====");
//
//        Object object =
//                new Employee(101, "Manoj", new Address("Bengaluru"));
//
//        if (object instanceof Employee(
//                int id,
//                String name,
//                Address(String city))) {
//
//            System.out.println("Employee ID   : " + id);
//            System.out.println("Employee Name : " + name);
//            System.out.println("Employee City : " + city);
//        }
//    }
//
//    /**
//     * 4. Pattern Matching for switch
//     *
//     * Switch can check an object's type, create a pattern variable,
//     * and execute type-specific logic.
//     */
//    private static void demonstratePatternMatchingSwitch() {
//
//        System.out.println("\n===== Pattern Matching for switch =====");
//
//        printObjectDetails(
//                new Employee(
//                        102,
//                        "Rahul",
//                        new Address("Mysuru")));
//
//        printObjectDetails("Java 19");
//
//        printObjectDetails(100);
//
//        printObjectDetails(null);
//    }
//
//    private static void printObjectDetails(Object object) {
//
//        String result = switch (object) {
//
//            case null ->
//                    "Value is null";
//
//            case Employee(
//                    int id,
//                    String name,
//                    Address(String city)) ->
//                    "Employee: id=%d, name=%s, city=%s"
//                            .formatted(id, name, city);
//
//            case String text ->
//                    "String value: " + text.toUpperCase();
//
//            case Integer number
//                    when number > 50 ->
//                    "Large integer: " + number;
//
//            case Integer number ->
//                    "Small integer: " + number;
//
//            default ->
//                    "Unknown object: "
//                            + object.getClass().getSimpleName();
//        };
//
//        System.out.println(result);
//    }
//
//    record Address(String city) {
//    }
//
//    record Employee(
//            int id,
//            String name,
//            Address address) {
//    }
//
//    record CustomerResponse(
//            String customerName,
//            double balance) {
//    }
//}
