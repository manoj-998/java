package test;

public class TestDL {

    private Node head;
    private Node tail;
    private int lenght;
    class Node {
        int val;
        Node next;
        public Node(int val){
            this.val=val;
        }
    }


    public void append(int val){
        Node newNode =new Node(val);
        if (head==null || tail ==null){
            head=newNode;
            tail=newNode;
        }
        else {
            tail.next=newNode;
            tail=tail.next;
        }
        lenght++;
    }



    public void printAll(){
            Node temp=head;
        System.out.println("Lenght "+lenght);
            while (temp!=null){

                System.out.println(" "+temp.val);
                temp=temp.next;
        }
    }


    public void removeLast(){
       Node temp=head;
       if (head.next==null)
       {
           tail=null;
           head=null;
       }else {
           while (temp!=null){
               if (temp.next.next==null){
                   tail=temp;
                   tail.next=null;
               }
               temp=temp.next;
           }
       }
        lenght--;
    }

    public void prepend(int val){
        Node newNode=new Node(val);
        if (head==null || tail==null){
            head=newNode;
            tail=newNode;
        }
        else {
            newNode.next=head;
            head=newNode;
        }
        lenght++;
    }

    public void removeFirst(){
        if (head==null){
            return;
        }
        if (head.next==null) {
            head=null;
            tail=null;
        }else {
            head=head.next;
        }
        lenght--;
    }

    public int getByIndex(int index){
        int val=0,i=0;
        if (head==null){
            return val;
        }
        Node temp=head;

        while (temp!=null && i<=index){
            if (i==index)
                val=temp.val;
            i++;
            temp=temp.next;
        }

        return val;
    }

    public void set(int index, int val) {
        Node newNode = new Node(val);
        if (head == null) {
            return;
        }

        if (index == 0) {
            newNode.next = head;
            head = newNode;
            lenght++;
            return;
        }
        Node temp = head;
        Node prep = head;
        int i = 0;
        while (i <= index && temp != null) {
            if (i == index) {
                prep.next = newNode;
                newNode.next = temp;
                lenght++;
                // ✅ handle tail update
                if (temp.next == null) {
                    tail = newNode;
                }
                return;
            }

            prep = temp;
            temp = temp.next;
            i++;
        }

    }

    public void remove(int index){
        if(head==null)
            return;

        Node temp =head;
        Node per = head;
        int i=0;
        while(temp!=null && i <= index){
            if (i==index){
                per.next=temp.next;
                temp.next=null;
                lenght--;
                return;
            }
            i++;
            per=temp;
            temp=temp.next;
        }

    }

    public void reverse(){

        Node curr=head;
        head=tail;
        tail=curr;

        Node before=null;
        Node after=curr.next;
        while (after!=null){
            after=curr.next;
            curr.next=before;
            before=curr;
            curr=after;
        }

    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public Node getTail() {
        return tail;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }
}

