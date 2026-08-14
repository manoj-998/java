package solid.liskov.solved;

public class LiskovSubstitutionDemo {

    public static void main(String[] args) {

        Bird sparrow = new Sparrow();
        Bird penguin = new Penguin();

        sparrow.eat();
        penguin.eat();

        FlyingBird flyingBird = new Sparrow();
        flyingBird.fly();
    }

    // Base abstraction
    static class Bird {

        public void eat() {
            System.out.println("Bird is eating");
        }
    }

    // Only birds that can fly extend this
    static class FlyingBird extends Bird {

        public void fly() {
            System.out.println("Bird is flying");
        }
    }

    static class Sparrow extends FlyingBird {

        @Override
        public void fly() {
            System.out.println("Sparrow is flying");
        }

        @Override
        public void eat() {
            super.eat();
        }
    }

    static class Penguin extends Bird {

        @Override
        public void eat() {
            System.out.println("Penguin is eating");
        }
    }
}