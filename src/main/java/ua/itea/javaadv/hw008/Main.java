package ua.itea.javaadv.hw008;

public class Main {
    public static void main(String[] args) {
        System.out.println("Creating Loader");
        Heap heap = new Heap(100);
        Loader l = new Loader(heap);
        Trolley trolley = new Trolley();
        l.setTrolley(trolley);
        System.out.println("Creating Unloader");
        Heap heapForUnloader = new Heap(0);
        Unloader u = new Unloader(heapForUnloader);
        System.out.println("Creating Transporter");
        Transporter t = new Transporter(l, u);
        l.setTransporter(t);
        System.out.println("Work in progress");

        l.join();
        t.join();
        u.join();

        System.out.println("Finished");
    }
}
