package basics.feature;

public class FunctionalInterfaceDemo {

    public static void main(String[] args) {

        // ==========================================
        // Lambda Expression
        // ==========================================
        Calculator<Integer> addition = (a, b) -> a + b;
        System.out.println("Addition : " + addition.calculate(10, 20));

        Calculator<Integer> multiplication = (a, b) -> a * b;
        System.out.println("Multiplication : " + multiplication.calculate(10, 20));

        // ==========================================
        // Default Method
        // ==========================================
        addition.display();

        // ==========================================
        // Static Method
        // ==========================================
        Calculator.info();

        // ==========================================
        // Passing Functional Interface
        // ==========================================
        execute(30, 10, addition);
        execute(30, 10, multiplication);

        // ==========================================
        // Method Reference
        // ==========================================
        Calculator<Integer> subtraction = MathOperation::subtract;

        System.out.println("Subtraction    : " + subtraction.calculate(30, 10));
    }


    /**
     * Accepts any Calculator implementation
     */
    public static void execute(Integer a, Integer b, Calculator<Integer> calculator) {

        System.out.println("Execute Result : " + calculator.calculate(a, b));
    }


    /**
     * Functional Interface
     */
    @FunctionalInterface
    interface Calculator<T extends Number> {

        int calculate(T a, T b);

        default void display() {
            System.out.println("Default Method");
        }

        static void info() {
            System.out.println("Static Method");
        }
    }

    /**
     * Used for Method Reference
     */
    static class MathOperation {

        public static int subtract(int a, int b) {
            return a - b;
        }
    }
}