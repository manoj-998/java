package recursion;

public class RecusrionMain {
    public static void main(String[] args) {
        //call Stack
        callOne();

        int number = 4;
        System.out.println(factorial(number));

    }

    public static int factorial(int number) {
        if (number == 1) return number;
        return number * factorial(number - 1);
    }

    public static void callOne() {
        callTwo();
        System.out.println("One");
    }

    public static void callTwo() {
        callThree();
        System.out.println("Two");
    }

    public static void callThree() {
        System.out.println("Three");
    }
}
