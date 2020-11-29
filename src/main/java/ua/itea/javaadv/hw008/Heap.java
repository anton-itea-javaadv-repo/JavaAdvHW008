package ua.itea.javaadv.hw008;

public class Heap {
    private int stock;
    private static final int HEAP_START_VALUE = 100;

    public Heap() {
        this(HEAP_START_VALUE);
    }

    public Heap(int stock) {
        this.stock = stock;
    }

    public int getStockCount() {
        return stock;
    }

    public boolean isExhausted() {
        return stock == 0;
    }

    public int getFromHeap(int portion) {
        int somethingInStock = stock;
        if (somethingInStock > 0) {
            if (somethingInStock > portion) {
                stock = somethingInStock - portion;
                return portion;
            } else {
                stock = 0;
                return somethingInStock;
            }
        } else {
            return 0;
        }
    }

    public void putToHeap(int portion) {
        stock += portion;
    }
}
