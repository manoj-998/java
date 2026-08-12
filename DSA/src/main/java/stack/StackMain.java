package stack;

public class StackMain {
    public static void main(String[] args) {

        StackDsa stack=new StackDsa(1);
        System.out.println("------------------------");
        System.out.println("Create new stack");
        stack.printAll();

        System.out.println("------------------------");
        System.out.println("push item to stack");
        stack.push(2);
        stack.push(3);
        stack.printAll();

        System.out.println("------------------------");
        System.out.println("Pop item from stack");
        stack.pop();
        stack.printAll();
        /*
        //TODO
        1. Stack: Push for a Stack That Uses an ArrayList ( ** Interview Question)
        2. Stack: Pop for a Stack That Uses an ArrayList ( ** Interview Question)
        3. Stack: Reverse String ( ** Interview Question)
        4. Stack: Parentheses Balanced ( ** Interview Question)
        5. Stack: Sort Stack ( ** Interview Question)
         */

    }
}
