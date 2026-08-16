package structuralDesginPattern.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates the Flyweight Design Pattern using
 * application notifications.
 * <p>
 * Flyweight Components:
 * 1. Flyweight          -> NotificationTemplate
 * 2. Concrete Flyweight -> EmailTemplate
 * 3. Flyweight Factory  -> TemplateFactory
 * 4. Context            -> Notification
 * 5. Client             -> main()
 */
public class FlyweightExample {

    /**
     * FLYWEIGHT
     * <p>
     * Defines the common operation for shared notification templates.
     * Template information is shared between multiple notifications.
     */
    interface NotificationTemplate {
        void send(String user, String email);
    }

    /**
     * CONCRETE FLYWEIGHT
     * <p>
     * Stores intrinsic (shared) state.
     * One EmailTemplate object can be reused by many notifications.
     */
    static class EmailTemplate implements NotificationTemplate {

        // Intrinsic State - shared
        private final String subject;
        private final String message;

        EmailTemplate(String subject, String message) {
            this.subject = subject;
            this.message = message;
        }

        /**
         * Sends an email using shared template data
         * and unique user information.
         *
         * @param user  unique user name
         * @param email unique email address
         */
        @Override
        public void send(String user, String email) {
            System.out.println("To: " + email);
            System.out.println("Subject: " + subject); //Passed
            System.out.println("Hi " + user + ", " + message);
            System.out.println("--------------------");
        }
    }

    /**
     * FLYWEIGHT FACTORY
     * <p>
     * Creates and caches Flyweight objects.
     * If the requested template already exists,
     * the same object is returned.
     */
    static class TemplateFactory {

        private static final Map<String, NotificationTemplate> cache =
                new HashMap<>();

        /**
         * Returns an existing template or creates a new one.
         *
         * @param type template type
         * @return shared NotificationTemplate
         */
        static NotificationTemplate getTemplate(String type) {

            if (!cache.containsKey(type)) {

                System.out.println("Creating template: " + type);

                if ("ORDER".equals(type)) {
                    cache.put(type,
                            new EmailTemplate(
                                    "Order Confirmed",
                                    "your order has been confirmed."
                            ));
                }

                if ("PAYMENT".equals(type)) {
                    cache.put(type,
                            new EmailTemplate(
                                    "Payment Successful",
                                    "your payment was successful."
                            ));
                }
            }

            return cache.get(type);
        }

        /**
         * Returns the number of Flyweight objects
         * currently stored in the cache.
         */
        static int getCacheSize() {
            return cache.size();
        }
    }

    /**
     * CONTEXT
     * <p>
     * Stores extrinsic (unique) state.
     * <p>
     * Every Notification has its own user and email,
     * but shares NotificationTemplate objects.
     */
    static class Notification {

        // Extrinsic State - unique
        private final String user;
        private final String email;

        // Shared Flyweight
        private final NotificationTemplate template;

        Notification(
                String user,
                String email,
                NotificationTemplate template) {

            this.user = user;
            this.email = email;
            this.template = template;
        }

        /**
         * Sends the notification using the shared template.
         */
        void send() {
            template.send(user, email);
        }
    }

    /**
     * CLIENT
     * <p>
     * Creates Context objects and gets shared
     * Flyweight objects through the Factory.
     */
    public static void main(String[] args) {

        /**
         * CLIENT FLOW:
         * First ORDER call -> cache miss -> creates and caches ORDER template.
         */
        NotificationTemplate orderTemplate =
                TemplateFactory.getTemplate("ORDER");

        /**
         * Uses already created ORDER template.
         * No cache lookup and no new template creation.
         */
        Notification n1 = new Notification(
                "John", "john@test.com", orderTemplate
        );

        /**
         * Reuses the same ORDER template used by n1.
         */
        Notification n2 = new Notification(
                "David", "david@test.com", orderTemplate
        );

        /**
         * Calls Factory again for ORDER.
         * Cache hit -> existing ORDER template is returned.
         * No new template is created.
         */
        Notification n3 = new Notification(
                "Alex",
                "alex@test.com",
                TemplateFactory.getTemplate("ORDER")
        );

        /**
         * First PAYMENT request.
         * Cache miss -> creates and caches a new PAYMENT template.
         */
        Notification n4 = new Notification(
                "Mike",
                "mike@test.com",
                TemplateFactory.getTemplate("PAYMENT")
        );

        /**
         * Each notification uses unique user/email data
         * with its shared template.
         */
        n1.send();
        n2.send();
        n3.send();
        n4.send();

        /**
         * Cache contains only 2 templates:
         * ORDER   -> shared by John, David and Alex
         * PAYMENT -> used by Mike
         *
         * Output: Templates created: 2
         */
        System.out.println(
                "Templates created: " + TemplateFactory.getCacheSize()
        );

        /**
         * Both calls request ORDER.
         * ORDER already exists -> cache hit both times.
         * Both variables receive the same shared object.
         */
        NotificationTemplate template1 =
                TemplateFactory.getTemplate("ORDER");

        NotificationTemplate template2 =
                TemplateFactory.getTemplate("ORDER");

        /**
         * '==' checks object reference.
         * Both point to the same cached ORDER template.
         *
         * Output: true
         */
        System.out.println(
                "Same ORDER template? " + (template1 == template2)
        );
    }
}