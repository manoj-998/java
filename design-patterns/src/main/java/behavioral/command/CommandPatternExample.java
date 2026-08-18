package behavioral.command;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Demonstrates Command Pattern using an order-processing system.
 *
 * Components:
 * 1. Command          -> Command
 * 2. Concrete Command -> CreateOrderCommand, CancelOrderCommand
 * 3. Receiver         -> OrderService
 * 4. Invoker          -> CommandExecutor
 * 5. Client           -> main()
 */
public class CommandPatternExample {

    /**
     * COMMAND
     * Common contract for all commands.
     */
    interface Command {
        void execute();
        void undo();
    }

    /**
     * RECEIVER
     * Contains the actual business logic.
     */
    static class OrderService {

        void createOrder(String orderId) {
            System.out.println("Order created: " + orderId);
        }

        void cancelOrder(String orderId) {
            System.out.println("Order cancelled: " + orderId);
        }

        void restoreOrder(String orderId) {
            System.out.println("Order restored: " + orderId);
        }
    }

    /**
     * CONCRETE COMMAND
     * Encapsulates the create-order request.
     */
    static class CreateOrderCommand implements Command {
        private final OrderService service;
        private final String orderId;

        CreateOrderCommand(OrderService service, String orderId) {
            this.service = service;
            this.orderId = orderId;
        }

        @Override
        public void execute() {
            service.createOrder(orderId);
        }

        @Override
        public void undo() {
            service.cancelOrder(orderId);
        }
    }

    /**
     * CONCRETE COMMAND
     * Encapsulates the cancel-order request.
     */
    static class CancelOrderCommand implements Command {
        private final OrderService service;
        private final String orderId;

        CancelOrderCommand(OrderService service, String orderId) {
            this.service = service;
            this.orderId = orderId;
        }

        @Override
        public void execute() {
            service.cancelOrder(orderId);
        }

        @Override
        public void undo() {
            service.restoreOrder(orderId);
        }
    }

    /**
     * INVOKER
     *
     * Stores commands in a queue and executes them later.
     * Also keeps the last executed command for undo.
     */
    static class CommandExecutor {
        private final Queue<Command> queue = new LinkedList<>();
        private Command lastCommand;

        /**
         * Adds command to queue.
         * Command is NOT executed immediately.
         */
        void submit(Command command) {
            queue.add(command);
            System.out.println("Command added to queue");
        }

        /**
         * Executes all commands from the queue.
         */
        void processCommands() {
            while (!queue.isEmpty()) {
                Command command = queue.poll();

                try {
                    command.execute();
                    lastCommand = command;
                } catch (Exception e) {
                    System.out.println("Command failed, retrying...");
                    queue.add(command);
                }
            }
        }

        /**
         * Undoes the last successfully executed command.
         */
        void undoLastCommand() {
            if (lastCommand != null) {
                lastCommand.undo();
                lastCommand = null;
            }
        }
    }

    /**
     * CLIENT
     *
     * Flow:
     * 1. Create Receiver.
     * 2. Create Commands.
     * 3. Submit Commands to Invoker.
     * 4. Invoker executes them later.
     * 5. Last command can be undone.
     */
    public static void main(String[] args) {

        /**
         * RECEIVER
         * Actual business operations are inside OrderService.
         */
        OrderService orderService = new OrderService();

        /**
         * CREATE COMMAND OBJECTS
         *
         * Requests are now objects.
         * They can be stored, queued, retried or undone.
         */
        Command createOrder1 = new CreateOrderCommand(orderService, "ORD-101");

        Command createOrder2 = new CreateOrderCommand(orderService, "ORD-102");

        Command cancelOrder = new CancelOrderCommand(orderService, "ORD-101");

        /**
         * INVOKER
         * Responsible for managing command execution.
         */
        CommandExecutor executor = new CommandExecutor();

        /**
         * Commands are only added to queue here.
         * Nothing is executed yet.
         */
        executor.submit(createOrder1);
        executor.submit(createOrder2);
        executor.submit(cancelOrder);

        /**
         * Now Invoker processes the queue.
         *
         * Flow:
         * CreateOrderCommand -> OrderService.createOrder()
         * CreateOrderCommand -> OrderService.createOrder()
         * CancelOrderCommand -> OrderService.cancelOrder()
         */
        executor.processCommands();

        /**
         * Last successful command was CancelOrderCommand.
         *
         * undo() calls:
         * OrderService.restoreOrder("ORD-101")
         */
        executor.undoLastCommand();
    }
}