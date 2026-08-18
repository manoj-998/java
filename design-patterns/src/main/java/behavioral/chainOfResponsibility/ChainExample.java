package behavioral.chainOfResponsibility;

/**
 * Chain of Responsibility example for API request processing.
 *
 * Flow:
 * Authentication -> Authorization -> Validation -> Rate Limit -> Processing
 */
public class ChainExample {

    /**
     * Request object passed through the chain.
     */
    static class Request {
        String user;
        String role;
        String payload;

        Request(String user, String role, String payload) {
            this.user = user;
            this.role = role;
            this.payload = payload;
        }
    }

    /**
     * HANDLER
     * Base class for all handlers.
     * Stores reference to the next handler.
     */
    abstract static class Handler {
        protected Handler next;

        /**
         * Connects this handler with the next handler.
         */
        Handler setNext(Handler next) {
            this.next = next;
            return next;
        }

        /**
         * Passes request to next handler.
         */
        void next(Request request) {
            if (next != null) {
                next.handle(request);
            }
        }

        abstract void handle(Request request);
    }

    /**
     * CONCRETE HANDLER
     * Checks whether user is authenticated.
     */
    static class AuthenticationHandler extends Handler {

        @Override
        void handle(Request request) {
            if (request.user == null || request.user.isBlank()) {
                System.out.println("Authentication failed");
                return;
            }

            System.out.println("Authentication passed");
            next(request);
        }
    }

    /**
     * CONCRETE HANDLER
     * Checks whether user has required role.
     */
    static class AuthorizationHandler extends Handler {

        @Override
        void handle(Request request) {
            if (!"ADMIN".equals(request.role)) {
                System.out.println("Authorization failed");
                return;
            }

            System.out.println("Authorization passed");
            next(request);
        }
    }

    /**
     * CONCRETE HANDLER
     * Validates request payload.
     */
    static class ValidationHandler extends Handler {

        @Override
        void handle(Request request) {
            if (request.payload == null || request.payload.isBlank()) {
                System.out.println("Validation failed");
                return;
            }

            System.out.println("Validation passed");
            next(request);
        }
    }

    /**
     * CONCRETE HANDLER
     * Simulates rate-limit check.
     */
    static class RateLimitHandler extends Handler {

        private int requestCount = 0;
        private final int limit = 2;

        @Override
        void handle(Request request) {
            requestCount++;

            if (requestCount > limit) {
                System.out.println("Rate limit exceeded");
                return;
            }

            System.out.println("Rate limit passed");
            next(request);
        }
    }

    /**
     * FINAL HANDLER
     * Performs actual business operation.
     */
    static class OrderHandler extends Handler {

        @Override
        void handle(Request request) {
            System.out.println(
                    "Order created for user: " + request.user
            );
        }
    }

    /**
     * CLIENT
     *
     * Creates and connects handlers.
     *
     * Chain:
     * Authentication
     *      ->
     * Authorization
     *      ->
     * Validation
     *      ->
     * RateLimit
     *      ->
     * OrderHandler
     */
    public static void main(String[] args) {

        Handler authentication = new AuthenticationHandler();
        Handler authorization = new AuthorizationHandler();
        Handler validation = new ValidationHandler();
        Handler rateLimit = new RateLimitHandler();
        Handler orderHandler = new OrderHandler();

        /**
         * Build the chain.
         */
        authentication.setNext(authorization)
                .setNext(validation)
                .setNext(rateLimit)
                .setNext(orderHandler);

        /**
         * REQUEST 1
         *
         * All checks pass.
         * Request reaches OrderHandler.
         */
        Request request1 = new Request("john", "ADMIN", "Create Order");
        authentication.handle(request1);

        System.out.println("---------------");
        /**
         * REQUEST 2
         *
         * Authentication passes.
         * Authorization fails because role is USER.
         *
         * Chain stops at AuthorizationHandler.
         */
        Request request2 = new Request("david", "USER", "Create Order");
        authentication.handle(request2);
        System.out.println("---------------");

        /**
         * REQUEST 3
         *
         * Authorization passes,
         * but payload is empty.
         *
         * Chain stops at ValidationHandler.
         */
        Request request3 = new Request("alex", "ADMIN", "");
        authentication.handle(request3);
        System.out.println("---------------");

        /**
         * REQUEST 4
         *
         * Passes all checks.
         * This is second valid request for RateLimitHandler.
         */
        Request request4 = new Request("mike", "ADMIN", "Create Order");
        authentication.handle(request4);
        System.out.println("---------------");

        /**
         * REQUEST 5
         *
         * Authentication, Authorization and Validation pass.
         *
         * RateLimitHandler has already accepted 2 requests,
         * so this request is blocked.
         */
        Request request5 = new Request("sam", "ADMIN", "Create Order");
        authentication.handle(request5);
    }
}