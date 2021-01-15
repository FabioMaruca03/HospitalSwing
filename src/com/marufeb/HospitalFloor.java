package com.marufeb;

import java.io.Serializable;

public abstract class HospitalFloor implements Serializable {
    protected String name;
    protected double area;

    public HospitalFloor() {
        name = "unnamed";
        area = 10;
    }

    public HospitalFloor(String name) {
        this.name = name;
    }

    public HospitalFloor(String name, double area) {
        this.name = name;
        this.area = area;
    }

    public abstract void onLoad();

    public abstract void onSave();

    public abstract void setPatients(Patient[] toArray);
}
