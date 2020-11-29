package ua.itea.javaadv.hw008;

public class Trolley {
    private final Heap trolleyHeap;

    public Trolley() {
        this.trolleyHeap = new Heap(0);
    }

    public Trolley(int trolleyHeap) {
        this.trolleyHeap = new Heap(trolleyHeap);
    }

    public int getCount() {
        return trolleyHeap.getStockCount();
    }

    public boolean isExhausted() {
        return trolleyHeap.isExhausted();
    }

    public boolean isFull() {
        return trolleyHeap.getStockCount() == 6;
    }

    public int getFromTrolleyHeap(int portion) {
        return this.trolleyHeap.getFromHeap(portion);
    }

    public void putToTrolleyHeap(int portion) {
        this.trolleyHeap.putToHeap(portion);
    }
}
