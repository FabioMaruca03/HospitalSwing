package com.marufeb;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class BackupExecutor extends Thread{

    private final static File backup = new File(System.getProperty("user.home")+File.separatorChar+"hospital.back");
    private final Hospital hospital;

    public BackupExecutor(Hospital hospital) {
        System.out.println("New backup mechanism initialised");
        this.hospital = hospital;
        start();
    }

    @Override
    public void run() {
        try {
            Hospital.save(hospital, backup);
            System.out.println("[i] BACKUP");
            Thread.sleep(TimeUnit.SECONDS.toMillis(30));
        } catch (InterruptedException ignore) { }
    }
}
