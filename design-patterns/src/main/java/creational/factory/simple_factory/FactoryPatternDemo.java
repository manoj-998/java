package creational.factory.simple_factory;

public class FactoryPatternDemo {

    public static void main(String[] args) {

        Notification email =
                NotificationFactory.create("EMAIL");

        email.send();


        Notification sms =
                NotificationFactory.create("SMS");

        sms.send();
    }


    // Product Interface
    interface Notification {

        void send();
    }


    // Concrete Product
    static class EmailNotification
            implements Notification {

        @Override
        public void send() {
            System.out.println("Email sent");
        }
    }


    // Concrete Product
    static class SmsNotification
            implements Notification {

        @Override
        public void send() {
            System.out.println("SMS sent");
        }
    }


    // Factory
    static class NotificationFactory {

        public static Notification create(String type) {

            if ("EMAIL".equalsIgnoreCase(type)) {
                return new EmailNotification();
            }

            if ("SMS".equalsIgnoreCase(type)) {
                return new SmsNotification();
            }

            throw new IllegalArgumentException(
                    "Invalid notification type"
            );
        }
    }
}