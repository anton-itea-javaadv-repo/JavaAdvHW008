package ua.itea.javaadv.hw008;

import java.util.concurrent.TimeUnit;

public class Unloader implements Runnable {
    private final Thread unloaderThread;

    private static final int PORTION = 2;
    private Trolley trolley = null;
    private final Heap heapToLoad;
    private Transporter transporter;

    private boolean shouldContinue = true;

    public Unloader(Heap heapToLoad) {
        this.heapToLoad = heapToLoad;

        this.unloaderThread = new Thread(this);
        this.unloaderThread.start();
    }

    public void join() {
        try {
            unloaderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Trolley getTrolley() {
        Trolley trolley = this.trolley;
        this.trolley = null;
        return trolley;
    }

    public synchronized void setTrolley(Trolley trolley, boolean shouldContinue) {
        this.shouldContinue = shouldContinue;
        this.trolley = trolley;
        System.out.println("Unloader got trolley with [" + trolley.getCount() + "], going back to work.");
        notify();
    }

    private synchronized void checkTrolleyIsExhausted() {
        if (trolley == null || trolley.isExhausted()) {
            try {
                System.out.println("Unloader is waiting.");
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

    @Override
    public void run() {
        while (shouldContinue) {
            checkTrolleyIsExhausted();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int fromTrolleyHeap = trolley.getFromTrolleyHeap(PORTION);
            System.out.print("Unloader getting " + fromTrolleyHeap + " of smth from trolley.");
            heapToLoad.putToHeap(fromTrolleyHeap);
            System.out.println(" Trolley have " + trolley.getCount() + " now. Heap have " + heapToLoad.getStockCount() + ".");
            if (trolley.isExhausted() && transporter != null) {
                System.out.print("Trolley exhausted. Unloader's heap is: "
                        + heapToLoad.getStockCount() + ".");
                if (shouldContinue) {
                    System.out.println(" Unloader passes the trolley to transporter.");
                    transporter.setTrolley(getTrolley(), true);
                } else {
                    System.out.println();
                }
                clearTransporter();
            }
        }
        System.out.println("Unloader finished work.");
    }
}
