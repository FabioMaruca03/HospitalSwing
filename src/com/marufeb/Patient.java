package com.marufeb;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Patient implements Serializable {
    private final UUID id = UUID.randomUUID();
    public void setName(String name) {
        this.name = name;
    }
    public Date getHospitalized() {

        return hospitalized;
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

    private Date hospitalized;

    public Patient(String name, int age, String description, Date hospitalized) {
        this.name = name;
        this.age = age;
        this.description = description;
        this.hospitalized = hospitalized;
    }

    public String getName() {
        return name;
    }


    private int age;
    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setHospitalized(Date hospitalized) {
        this.hospitalized = hospitalized;
    }


    private HospitalRoom room = null;
    private String description, name;
    public HospitalRoom getRoom() {
        return room;
    }

    public Patient(String name, int age, String description, Date hospitalized, HospitalRoom room) {
        this.name = name;
        this.age = age;
        this.description = description;
        this.hospitalized = hospitalized;
        this.room = room;
    }

    public void setRoom(HospitalRoom room) {
        this.room = room;
    }
}
