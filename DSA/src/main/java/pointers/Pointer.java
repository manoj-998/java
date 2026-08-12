package pointers;

public class Pointer {
    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public Pointer getNode() {
        return node;
    }

    public void setNode(Pointer node) {
        this.node = node;
    }

    public int a;
    public Pointer node;

    public Pointer(int value) {
        this.a = value;
    }


}
