package bigO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BigONClass {

    /**
     * This is big O(n)
     *
     * @param n big O(n)
     */
    public void print(int n) {
        for (int i = 0; i < n; i++) {
            System.out.println(i);
        }
    }

    /**
     * This is big O(n)
     * big O(n+n) = big O(2n) = big O(n) rule to remove constants
     */
    public void printONPlusN(int n) {
        for (int i = 0; i < n; i++) {
            System.out.println(i);
        }
        for (int j = 0; j < n; j++) {
            System.out.println(j);
        }
    }

    /**
     * This is big O(n^2)
     */
    public void printOnSquare(int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.println(i + "" + j);
            }
        }
    }

    /**
     * This is big O(n^2)
     * big O(n^2+n) = big O(n^2) rule to remove non-dominants
     */
    public void printOnSquareDropNonDominants(int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.println(i + "" + j);
            }
        }
        for (int j = 0; j < n; j++) {
            System.out.println(j);
        }
    }

    /**
     * This is big O(1)
     * big O(1+1+1) = big O(3) = big O(1) to simplify
     * called constant time
     */
    public int printOofOne(int n) {
        return n + n + n;
    }

    /**
     * This code is binary search with big O(log n) time complexity
     *
     * <p>
     * when there is 8 items => big 2^3= 8 -> log2(8) = ? -> log2(8) = 3 how may times it takes to get to 1 item
     * in Billion records:- log2(1067302992) = 31 this works good when there is larger item to search
     *
     * </P>
     */
    public int printOofLogN(int arr[], int l, int r, int x) {
        arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        l = 0;
        r = arr.length - 1;
        x = 1; //Key to find

        while (l <= r) {
            int mid = (l + r) / 2;
            if (arr[mid] == x) {
                return mid;
            } else if (arr[mid] > x) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return -1;
    }

    /**
     * This id big O(n+m)
     * Because n and m can have different values
     */
    public void printBigONPlusN(int n, int m) {
        for (int i = 0; i < n; i++) {
            System.out.println(i);
        }
        for (int j = 0; j < m; j++) {
            System.out.println(j);
        }
    }

    /**
     * This id big O(n*m)
     * Because n and m can have different values
     */
    public void printBigONMulN(int n, int m) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.println(i + " " + j);
            }

        }
    }


    public void bigOExample() {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        // big O(1) this is done in the end of array list
        list.add(11);
        list.remove(list.size() - 1);

        //big O(n) because we need to update the indexing of the list
        //big O(1/2n) = big O(n) rule to remove constants to add item in middle
        list.remove(0);
        list.add(0, 0);
    }


    /**
     * Space complexity O(1)
     * <p>
     * Input space – memory used to store the input
     * <p>
     * Auxiliary space – extra memory used by the algorithm
     * <p>
     * Uses a fixed number of variables
     * Memory does not depend on input size
     */
    public int sum(int a, int b) {
        return a + b;
    }


    /**
     * Space complexity O(n)
     * *  Extra array of size n Memory grows with input size
     */
    public int[] copyArray(int[] arr) {
        int[] copy = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        return copy;
    }


    /**
     * Recursive Function — O(n) (stack space)
     * Each recursive call uses stack memory
     * <p>
     * Depth of recursion = n
     */
    public int factorial(int n) {
        if (n == 1) return 1;
        return n * factorial(n - 1);
    }


    /**
     * Reference
     */
    public void reference(){
        int a=10;
        int b=12;
        int c=a;//It stores reference to a

        Map<String,String> map1=Map.of("1","1");
        Map<String,String> map2=map1;//It stores reference to a map1
    }

}
