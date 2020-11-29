package ua.itea.javaadv.hw008;

import java.util.concurrent.TimeUnit;

public class Loader implements Runnable {
    private final Thread loaderThread;

    private static final int PORTION = 3;
    private Trolley trolley = null;
    private final Heap heapToUnload;

    private Transporter transporter;

    public Loader(Heap heapToUnload) {
        this.heapToUnload = heapToUnload;

        this.loaderThread = new Thread(this);
        this.loaderThread.start();
    }

    public void join() {
        try {
            loaderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Trolley getTrolley() {
        Trolley trolley = this.trolley;
        this.trolley = null;
        return trolley;
    }

    public synchronized void setTrolley(Trolley trolley) {
        this.trolley = trolley;
        System.out.println("Loader got trolley, going back to work.");
        notify();
    }

    private synchronized void checkNoTrolleyOrTrolleyIsFull() {
        if (trolley == null || trolley.isFull()) {
            try {
                System.out.println("Loader is waiting.");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearTransporter() {
        this.transporter = null;
    }

    public void setTransporter(Transporter transporter) {
        this.transporter = transporter;
    }

    @java.lang.Override
    public void run() {
        while (!heapToUnload.isExhausted()) {
            checkNoTrolleyOrTrolleyIsFull();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int fromHeap = heapToUnload.getFromHeap(PORTION);
            System.out.print("Loader put " + fromHeap + " of smth to trolley.");
            trolley.putToTrolleyHeap(fromHeap);
            System.out.println(" Trolley have " + trolley.getCount() + " now. Heap have " + heapToUnload.getStockCount() + ".");
            if ((trolley.isFull() || heapToUnload.isExhausted()) && transporter != null) {
                System.out.println("Loading done. Loader's heap is: "
                        + heapToUnload.getStockCount()
                        + ". Loader passes the trolley to transporter.");
                transporter.setTrolley(getTrolley(), !heapToUnload.isExhausted());
                clearTransporter();
            }
        }
        System.out.println("Loader finished work.");
    }
}
