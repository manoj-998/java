package basics.feature;

public class DefaultMethodDemo {

    public static void main(String[] args) {

        Car car = new Car();

        car.start();
    }

    /**
     * ### Why were default methods introduced?
     * To allow adding new methods to interfaces without forcing all existing implementing classes to implement them.
     */
    interface Vehicle {
        default void start() {
            System.out.println("Vehicle Started");
        }
    }

    static class Car implements Vehicle {

        @Override
        public void start() {
            System.out.println("Car Started");
        }
    }
}