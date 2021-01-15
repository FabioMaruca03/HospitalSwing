package com.marufeb;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Patient implements Serializable {
    private HospitalRoom room = null;
    private String description, name;
    private Date hospitalized;
    private int age;
    private final UUID id = UUID.randomUUID();

    public Patient(String name, int age, String description, Date hospitalized, HospitalRoom room) {
        this.name = name;
        this.age = age;
        this.description = description;
        this.hospitalized = hospitalized;
        this.room = room;
    }

    public Patient(String name, int age, String description, Date hospitalized) {
        this.name = name;
        this.age = age;
        this.description = description;
        this.hospitalized = hospitalized;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getHospitalized() {
        return hospitalized;
    }

    public void setHospitalized(Date hospitalized) {
        this.hospitalized = hospitalized;
    }

    public HospitalRoom getRoom() {
        return room;
    }

    public void setRoom(HospitalRoom room) {
        this.room = room;
    }
}
