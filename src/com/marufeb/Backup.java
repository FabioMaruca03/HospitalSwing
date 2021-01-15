package com.marufeb;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Backup extends Thread{

    private final static File storage = new File(System.getProperty("user.home")+File.separatorChar+"hospital.back");
    private final Hospital h;

    @Override
    public void run() {
        try {
            Hospital.save(h, storage);
            Thread.sleep(TimeUnit.SECONDS.toMillis(30));
        } catch (InterruptedException ignore) { }
    }

    public Backup(Hospital h) {
        this.h = h;
        start();
    }
}
