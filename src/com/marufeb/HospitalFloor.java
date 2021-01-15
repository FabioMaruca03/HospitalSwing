package com.marufeb;

import java.io.Serializable;

public abstract class HospitalFloor implements Serializable {
    public abstract void onSave();

    public abstract void onLoad();

    public abstract void setPatients(Patient[] toArray);

    public HospitalFloor() {
        name = "floor";
    }

    public HospitalFloor(String name) {
        this.name = name;
    }

    protected String name;
}
