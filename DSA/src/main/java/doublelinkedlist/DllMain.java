package doublelinkedlist;

import common.Common;

public class DllMain {
    public static void main(String[] args) {

        DoublyLinkedListDsa ddlList = new DoublyLinkedListDsa(1);
        ddlList.printAll();

        System.out.println(Common.lineSperator);
        System.out.println("Append to DDL");
        ddlList.append(2);
        ddlList.printAll();

        System.out.println(Common.lineSperator);
        System.out.println("Remove last in DDL");
        System.out.println("removed : " + ddlList.removeLast());
        ddlList.printAll();

        System.out.println(Common.lineSperator);
        System.out.println("prepend in DDL");
        ddlList.prepend(0);
        ddlList.printAll();

        System.out.println(Common.lineSperator);
        System.out.println("removeFist in DDL");
        ddlList.removeFirst();
        ddlList.printAll();

        System.out.println(Common.lineSperator);
        System.out.println("get item index in DDL");
        ddlList.prepend(0);
        ddlList.append(2);
        ddlList.append(3);
        ddlList.append(4);
        ddlList.append(5);
        ddlList.printAll();
        System.out.println("Index value : " + ddlList.get(3).value);

        System.out.println(Common.lineSperator);
        System.out.println("set item value in index DDL");
        ddlList.set(3, 1);
        ddlList.printAll();

        System.out.println(Common.lineSperator);
        System.out.println("insert item index in DDL");
        ddlList.set(3, 3);
        ddlList.insertInIndex(6, 6);
        ddlList.printAll();

        System.out.println(Common.lineSperator);
        System.out.println("remove item index in DDL");
        ddlList.removeInIndex(5);
        ddlList.printAll();

        System.out.println(Common.lineSperator);
        System.out.println("swap first and last in DDL");
        ddlList.swapFirstLast();
        ddlList.printAll();

        System.out.println(Common.lineSperator);
        System.out.println("Reverse");
        ddlList.revers();
        ddlList.printAll();
/*
//TODO
1.DLL: Palindrome Checker ( ** Interview Question)
2.DLL: Swap Nodes in Pairs ( ** Interview Question)
 */
    }
}
