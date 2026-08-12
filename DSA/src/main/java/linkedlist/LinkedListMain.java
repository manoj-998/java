package linkedlist;

import common.Common;

public class LinkedListMain {
    public static void main(String[] args) {

        LinkedListDsa linkedListDsa = new LinkedListDsa(1);
        linkedListDsa.append(2);
        linkedListDsa.append(3);
        linkedListDsa.append(4);
        linkedListDsa.printAll();

        System.out.println(Common.lineSperator);
        System.out.println("prepend");
        linkedListDsa.prepend(0);
        linkedListDsa.prepend(4);
        linkedListDsa.printAll();

        System.out.println(Common.lineSperator);
        System.out.println("start removing all");
        System.out.println(linkedListDsa.removeLast().value);
        System.out.println(linkedListDsa.removeLast().value);
        System.out.println(linkedListDsa.removeLast().value);
        System.out.println(linkedListDsa.removeLast().value);
        linkedListDsa.printAll();


        System.out.println(Common.lineSperator);
        System.out.println("Remove from First");
        System.out.println(linkedListDsa.removeFirst().value);
        linkedListDsa.removeFirst();
        linkedListDsa.printAll();
        System.out.println(linkedListDsa.removeLast());


        System.out.println(Common.lineSperator);
        System.out.println("get Index");
        linkedListDsa.append(2);
        linkedListDsa.prepend(1);
        linkedListDsa.prepend(3);
        linkedListDsa.printAll();
        System.out.println("get index 0-" + linkedListDsa.getIndex(0).value);
        System.out.println("get index 1-" + linkedListDsa.getIndex(1).value);
        System.out.println("get index 2-" + linkedListDsa.getIndex(2).value);
        System.out.println("get index 3-" + linkedListDsa.getIndex(3));


        System.out.println(Common.lineSperator);
        System.out.println("Set value at index");
        linkedListDsa.setIndex(0, 0);
        linkedListDsa.printAll();


        System.out.println(Common.lineSperator);
        System.out.println("Insert in a index");
        System.out.println(linkedListDsa.insertInIndex(2, 3));
        linkedListDsa.removeLast();
        linkedListDsa.removeLast();
        linkedListDsa.removeLast();
        linkedListDsa.removeLast();
        linkedListDsa.insertInIndex(0, 0);
        linkedListDsa.insertInIndex(1, 1);
        linkedListDsa.printAll();


        System.out.println(Common.lineSperator);
        System.out.println("Remove in the Index");
        System.out.println("remove index 1 - " + linkedListDsa.removeIndex(1).value);
        System.out.println("remove index 0- " + linkedListDsa.removeIndex(0).value);
        linkedListDsa.printAll();


        System.out.println(Common.lineSperator);
        System.out.println("Reverse a linked list");
        linkedListDsa.append(1);
        linkedListDsa.append(2);
        linkedListDsa.append(3);
        linkedListDsa.printList();
        System.out.println("After reverse");
        linkedListDsa.reverse();
        linkedListDsa.printList();


        System.out.println(Common.lineSperator);
        System.out.println("Find Middle vale");
        linkedListDsa.append(2);
        linkedListDsa.append(3);
        linkedListDsa.append(4);
        linkedListDsa.append(5);
        System.out.println("middle value for Odd " + linkedListDsa.findMiddleNode().value);
        linkedListDsa.append(6);
        System.out.println("middle value for Even " + linkedListDsa.findMiddleNode().value);
        linkedListDsa.printAll();


        System.out.println(Common.lineSperator);
        System.out.println("Find Loop in linked List (Floyd's cycle-finding algorith)");
        System.out.println(linkedListDsa.hasLoop());


        System.out.println(Common.lineSperator);
        System.out.println("Find Kth Node From End");
        int k = 2;
        System.out.println(linkedListDsa.findKthFromEnd(k).value);


        System.out.println(Common.lineSperator);
        System.out.println("Partition List");
        System.out.println("You have a singly linked list that DOES NOT HAVE A TAIL POINTER  " +
                "(which will make this method simpler to implement).\n" +
                "Given a value x you need to rearrange the linked list such that all nodes with a value" +
                " less than x come before all nodes with a value greater than or equal to x.");
        linkedListDsa.printAll();
        linkedListDsa.partitionList(3);
        linkedListDsa.printAll();

        System.out.println(Common.lineSperator);
        System.out.println("Remove duplicate");
        //using set
        linkedListDsa.removeDuplicatesN();
        linkedListDsa.removeDuplicatesN2();
        linkedListDsa.printAll();


        /*
   //     TODO:
        1. LL: Binary to Decimal ( ** Interview Question)
        2. LL: Reverse Between ( ** Interview Question)
        */

    }
}
