package bigO;

public class Demo {
    int x = 10;
    int display()
    {
        System.out.println("x = " + x);
        return 0;
    }
}

class Main {
    public static void main(String[] args)
    {
        Demo D1 = new Demo();
        Demo G1 = D1;
        Demo M1 = new Demo();
        Demo Q1 = M1;

        // updating the value of x using G! reference variable
        G1.x = 25;

        System.out.println(G1.x); // Point 1
        System.out.println(D1.x); // Point 2
        System.out.println(M1.x);
        System.out.println(Q1.x);
    }
}
