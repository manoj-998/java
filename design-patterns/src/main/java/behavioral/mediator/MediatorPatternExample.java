package behavioral.mediator;

/**
 * Mediator Pattern Example - Order Workflow
 *
 * Components:
 * 1. Mediator          -> OrderMediator
 * 2. ConcreteMediator  -> OrderMediatorImpl
 * 3. Colleagues        -> PaymentService, InventoryService,
 *                         ShippingService, NotificationService
 * 4. Client            -> main()
 */
public class MediatorPatternExample {

    /**
     * MEDIATOR
     * Defines communication between services.
     */
    interface OrderMediator {
        void notify(Component sender, String event);
    }

    /**
     * BASE COLLEAGUE
     * Every service keeps reference to the Mediator.
     */
    abstract static class Component {
        protected final OrderMediator mediator;
        Component(OrderMediator mediator) {
            this.mediator = mediator;
        }
    }

    /**
     * COLLEAGUE
     * Handles payment.
     */
    static class PaymentService extends Component {
        PaymentService(OrderMediator mediator) {
            super(mediator);
        }

        void processPayment() {
            System.out.println("Payment completed");
            // Notify Mediator instead of calling Inventory directly.
            mediator.notify(this, "PAYMENT_COMPLETED");
        }
    }

    /**
     * COLLEAGUE
     * Handles inventory reservation.
     */
    static class InventoryService extends Component {

        InventoryService(OrderMediator mediator) {
            super(mediator);
        }
        void reserveStock() {
            System.out.println("Inventory reserved");
            // Notify Mediator when inventory is ready.
            mediator.notify(this, "INVENTORY_RESERVED");
        }
    }

    /**
     * COLLEAGUE
     * Handles shipping.
     */
    static class ShippingService extends Component {
        ShippingService(OrderMediator mediator) {
            super(mediator);
        }
        void createShipment() {
            System.out.println("Shipment created");
            // Notify Mediator after shipment creation.
            mediator.notify(this, "SHIPMENT_CREATED");
        }
    }

    /**
     * COLLEAGUE
     * Handles notification.
     */
    static class NotificationService extends Component {
        NotificationService(OrderMediator mediator) {
            super(mediator);
        }
        void sendConfirmation() {
            System.out.println("Order confirmation sent");
        }
    }

    /**
     * CONCRETE MEDIATOR
     *
     * Contains only coordination logic.
     * Individual services still own their business logic.
     */
    static class OrderMediatorImpl implements OrderMediator {

        private PaymentService paymentService;
        private InventoryService inventoryService;
        private ShippingService shippingService;
        private NotificationService notificationService;

        void setPaymentService(PaymentService paymentService) {
            this.paymentService = paymentService;
        }

        void setInventoryService(InventoryService inventoryService) {
            this.inventoryService = inventoryService;
        }

        void setShippingService(ShippingService shippingService) {
            this.shippingService = shippingService;
        }

        void setNotificationService(NotificationService notificationService) {
            this.notificationService = notificationService;
        }

        /**
         * Decides what should happen for each event.
         */
        @Override
        public void notify(Component sender, String event) {

            if ("PAYMENT_COMPLETED".equals(event)) {
                System.out.println("Mediator: payment done -> reserve inventory");
                inventoryService.reserveStock();
            }

            else if ("INVENTORY_RESERVED".equals(event)) {
                System.out.println("Mediator: inventory ready -> create shipment");
                shippingService.createShipment();
            }

            else if ("SHIPMENT_CREATED".equals(event)) {
                System.out.println("Mediator: shipment created -> notify customer");
                notificationService.sendConfirmation();
            }
        }

        /**
         * Starts the complete order workflow.
         */
        void placeOrder() {
            System.out.println("Starting order workflow...");
            paymentService.processPayment();
        }
    }

    /**
     * CLIENT
     *
     * Flow:
     * 1. Create Mediator.
     * 2. Create all Colleagues.
     * 3. Register Colleagues with Mediator.
     * 4. Start workflow through Mediator.
     */
    public static void main(String[] args) {

        // 1. Create Concrete Mediator.
        OrderMediatorImpl mediator = new OrderMediatorImpl();

        // 2. Create Colleagues and give them Mediator reference.
        PaymentService payment = new PaymentService(mediator);
        InventoryService inventory = new InventoryService(mediator);
        ShippingService shipping = new ShippingService(mediator);
        NotificationService notification = new NotificationService(mediator);

        // 3. Register all services inside Mediator.
        mediator.setPaymentService(payment);
        mediator.setInventoryService(inventory);
        mediator.setShippingService(shipping);
        mediator.setNotificationService(notification);

        /*
         * 4. Start order flow.
         *
         * placeOrder()
         *    ->
         * PaymentService
         *    ->
         * PAYMENT_COMPLETED
         *    ->
         * Mediator
         *    ->
         * InventoryService
         *    ->
         * INVENTORY_RESERVED
         *    ->
         * Mediator
         *    ->
         * ShippingService
         *    ->
         * SHIPMENT_CREATED
         *    ->
         * Mediator
         *    ->
         * NotificationService
         */
        mediator.placeOrder();
    }
}