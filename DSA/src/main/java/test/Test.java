package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static test.ModificationType.ADD;


public class Test {
    public static void main(String[] args) {

        TestDL t=new TestDL();
        t.printAll();

        t.append(1);
        t.append(2);
        t.append(4);
        t.append(3);
        t.append(4);

        t.printAll();
        t.removeLast();
        t.printAll();

        TestDL t1=new TestDL();
        t1.append(1);
        t1.append(2);
        t1.printAll();
        t1.removeLast();
        t1.removeLast();
        t1.printAll();

        TestDL t2=new TestDL();
        t2.prepend(1);
        t2.prepend(0);
        t2.prepend(5);
        t2.printAll();

        TestDL t3=new TestDL();
        t3.append(1);
        t3.append(0);
        t3.append(2);
        t3.removeFirst();

        t3.printAll();

        TestDL t4=new TestDL();
        t4.append(0);
        t4.append(1);
        t4.append(2);
        t4.append(5);
        t4.printAll();

        System.out.println(t4.getByIndex(2));


        TestDL t5=new TestDL();
        t5.append(0);
        t5.append(1);
        t5.append(2);
        t5.append(5);
        t5.set(3,4);
        t5.printAll();

        TestDL t6=new TestDL();
        t6.append(1);
        t6.append(2);
        t6.append(3);
        t6.append(4);
        t6.printAll();
        t6.reverse();
        t6.printAll();

    }
}


