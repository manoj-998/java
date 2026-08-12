package queue;


public class QueueMain {
    public static void main(String[] args) {

        System.out.println("--------------------------");
        System.out.println("Crete new Queue");
        QueueDsa queue = new QueueDsa(1);
        queue.printAll();

        System.out.println("--------------------------");
        System.out.println("EnQueue adding item in last of queue");
        queue.enQueue(2);
        queue.printAll();

        System.out.println("--------------------------");
        System.out.println("DeQueue remove item first of queue");
        queue.deQueue();
        queue.printAll();

        /*
        //TODO
        1.Queue Using Stacks: Enqueue ( ** Interview Question)
        2.Queue Using Stacks: Dequeue ( ** Interview Question)
         */

    }
}
