package basics.thread.code;

public class ProducerConsumerDemo {

    public static void main(String[] args) {

        SharedResource resource = new SharedResource();
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    resource.produce(i);
                }
            } catch (Exception e) {
            }

        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    resource.consume();
                }
            } catch (Exception e) {
            }

        });

        producer.start();
        consumer.start();
    }
}