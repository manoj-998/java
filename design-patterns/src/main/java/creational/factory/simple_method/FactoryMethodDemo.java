package creational.factory.simple_method;


public class FactoryMethodDemo {

    public static void main(String[] args) {

        // Email factory
        NotificationFactory factory = new EmailFactory();
        Notification notification = factory.createNotification();
        notification.send();

        // SMS factory
        factory = new SmsFactory();
        notification = factory.createNotification();
        notification.send();
    }


    /*
                 NotificationFactory
                         |
              createNotification()
                         |
              ┌──────────┴──────────┐
              ↓                     ↓
        EmailFactory            SmsFactory
              |                     |
              ↓                     ↓
    EmailNotification       SmsNotification
     */
    // Product Interface
    interface Notification {
        void send();
    }

    // Concrete Product
    static class EmailNotification implements Notification {
        @Override
        public void send() {
            System.out.println("Email sent");
        }
    }

    // Concrete Factory
    static class EmailFactory extends NotificationFactory {
        @Override
        Notification createNotification() {
            return new EmailNotification();
        }
    }

    // Concrete Product
    static class SmsNotification implements Notification {
        @Override
        public void send() {
            System.out.println("SMS sent");
        }
    }

    // Concrete Factory
    static class SmsFactory extends NotificationFactory {

        @Override
        Notification createNotification() {
            return new SmsNotification();
        }
    }

    // Creator / Factory
    static abstract class NotificationFactory {
        // Factory Method
        abstract Notification createNotification();
    }






}