package ua.itea.javaadv.hw008;

import java.util.concurrent.TimeUnit;

public class Transporter implements Runnable {
    private final Thread transporterThread;

    private static final int PAUSE_SEC = 5;
    private boolean gotoUnloader;
    private Trolley trolley = null;
    private boolean shouldContinue = true;

    private final Loader loader;
    private final Unloader unloader;

    public Transporter(Loader loader, Unloader unloader) {
        this.loader = loader;
        this.unloader = unloader;

        this.gotoUnloader = true;
        transporterThread = new Thread(this);
        transporterThread.start();
    }

    public void join() {
        try {
            transporterThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Trolley getTrolley() {
        Trolley trolley = this.trolley;
        this.trolley = null;
        return trolley;
    }

    public synchronized void setTrolley(Trolley trolley, Boolean shouldContinue) {
        this.shouldContinue = shouldContinue;
        this.trolley = trolley;
        notify();
    }

    private synchronized void waitIfNoTrolley() {
        if (trolley == null) {
            try {
                System.out.println("Transporter is waiting.");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void exchangeTrolley() {
        if (gotoUnloader) {
            System.out.println("Transporter is giving trolley to unloader");
            unloader.setTrolley(getTrolley(), shouldContinue);
            unloader.setTransporter(this);
        } else {
            System.out.println("Transporter is giving trolley to loader");
            loader.setTrolley(getTrolley());
            loader.setTransporter(this);
        }
        gotoUnloader = !gotoUnloader;
    }

    @Override
    public void run() {
        while (shouldContinue) {
            waitIfNoTrolley();
            try {
                System.out.println("Transporter is working now.");
                for (int i = 1; i <= PAUSE_SEC; i++) {
                    System.out.print(".");
                    TimeUnit.SECONDS.sleep(1);
                }
                System.out.println();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            exchangeTrolley();
        }
        System.out.println("Transporter finished work.");
    }
}
