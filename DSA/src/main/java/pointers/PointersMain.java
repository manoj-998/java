package pointers;

import java.util.HashMap;
import java.util.Map;

public class PointersMain {
    public static void main(String[] args) {
        System.out.println("-------------------------------------");

        int a = 10;
        int b = a;
        System.out.println("a: " + a + ", b: " + b);
        a = 20;
        System.out.println("a: " + a + ", b: " + b);

        System.out.println("-------------------------------------");
        Map<String, Integer> mapOne = new HashMap<>();
        Map<String, Integer> mapTwo = new HashMap<>();

        mapOne.put("value", 1);
        mapTwo = mapOne;
        System.out.println("mapOne: " + mapOne + ", mapTwo: " + mapTwo);
        mapOne.put("value", 2);
        System.out.println("mapOne: " + mapOne + ", mapTwo: " + mapTwo);

        System.out.println("-------------------------------------");
        System.out.println("Pointer Assignment");
        Pointer working = new Pointer(1);
        working.node = new Pointer(2);
        working.node.node = new Pointer(3);
        System.out.println(working);
        System.out.println(working.node);
        System.out.println(working.node.node);
        System.out.println(working.getNode().getNode().a);

        System.out.println("-------------------------------------");
        System.out.println("Not working Pointer assignment");
        Pointer notWorkingHead = new Pointer(1);
        System.out.println(notWorkingHead);
        Pointer notWorkingTemp = notWorkingHead.node;// assign null to temp
        Pointer notWorkingnew = new Pointer(3);// create new node
        notWorkingTemp = notWorkingnew;//assign new node to temp which has no link to head node
        System.out.println(notWorkingTemp);
        System.out.println("below link won't working because there is not link be the nodes");
        System.out.println(notWorkingTemp.node.a);

    }
}
