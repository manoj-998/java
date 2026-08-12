package basics.thread.code;

class Tread extends Thread {

    @Override
    public void run() {
        System.out.println("Thread running: " + Thread.currentThread().getName());
    }

}