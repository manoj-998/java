package basics.feature;

public class SealedClassDemo {

    public static void main(String[] args) {
        Vehicle car = new Car("BMW");
        Vehicle bike = new Bike("Yamaha");
        Vehicle truck = new Truck("Volvo");

        car.start();
        bike.start();
        truck.start();
    }


    /**
     * Sealed Parent Class
     *
     * Only Car, Bike and Truck
     * are allowed to extend Vehicle.
     */
    sealed static abstract class Vehicle
            permits Car, Bike, Truck {

        protected String brand;

        public Vehicle(String brand) {
            this.brand = brand;
        }

        public abstract void start();
    }


    /**
     * Final Class
     *
     * Cannot be extended further.
     */
    static final class Car extends Vehicle {
        public Car(String brand) {
            super(brand);
        }
        @Override
        public void start() {
            System.out.println(brand + " Car Started");
        }
    }


    /**
     * Non-Sealed Class
     *
     * Can be extended by any class.
     */
    static non-sealed class Bike extends Vehicle {

        public Bike(String brand) {
            super(brand);
        }

        @Override
        public void start() {
            System.out.println(
                    brand + " Bike Started");
        }
    }


    /**
     * Child of Non-Sealed Class
     */
    static class SportsBike extends Bike {

        public SportsBike(String brand) {
            super(brand);
        }

        @Override
        public void start() {
            System.out.println(
                    brand + " Sports Bike Started");
        }
    }


    /**
     * Sealed Child
     *
     * Only ElectricTruck can extend it.
     */
    static sealed class Truck extends Vehicle
            permits ElectricTruck {

        public Truck(String brand) {
            super(brand);
        }

        @Override
        public void start() {
            System.out.println(
                    brand + " Truck Started");
        }
    }


    /**
     * Final Child
     */
    static final class ElectricTruck extends Truck {

        public ElectricTruck(String brand) {
            super(brand);
        }

        @Override
        public void start() {
            System.out.println(
                    brand + " Electric Truck Started");
        }
    }

}