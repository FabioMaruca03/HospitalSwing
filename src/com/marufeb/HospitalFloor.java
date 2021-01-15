package com.marufeb;

import java.io.Serializable;

public abstract class HospitalFloor implements Serializable {
    public HospitalFloor(String name) {
        this.name = name;
    }

    protected String name;

    public HospitalFloor() {
        name = "floor";
    }

    public abstract void setPatients(Patient[] toArray);

    public abstract void onSave();

    public abstract void onLoad();
}
