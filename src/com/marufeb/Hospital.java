package com.marufeb;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Hospital extends HospitalFloor {
    private String name = "undefined";
    public void setName(String name) {
        this.name = name;
    }

    public static void save(Hospital hospital, File file) {
        try {
            hospital.onSave();
            hospital.floors.forEach(HospitalFloor::onSave);
            final ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(hospital);
            stream.flush();
            stream.close();
        } catch (Exception ignore) {
            throw new IllegalStateException("File not supported: "+file.getAbsolutePath());
        }
    }

    public void remFloor(HospitalRoom floor) {
        if (floors.stream().anyMatch(f->f.name.equals(floor.name))) {
            floors.remove(floor);
        } else throw new IllegalStateException("A floor called "+floor.name+" is not defined");
    }

    @Override
    public void onLoad() {
        System.out.println("Loading hospital "+name);
    }

    public List<HospitalRoom> getFloors() {
        return floors;
    }

    public static Hospital load(File file) {
        try {
            final ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
            final Hospital hospital = (Hospital) stream.readObject();
            hospital.onLoad();
            hospital.floors.forEach(HospitalFloor::onLoad);
            return hospital;
        } catch (Exception ignore) {
            throw new IllegalStateException("File not supported: "+file.getAbsolutePath());
        }
    }

    public String getName() {
        return name;
    }

    private List<HospitalRoom> floors = new ArrayList<>();

    public void addFloor(HospitalRoom floor) {
        if (floors.stream().noneMatch(f->f.name.equals(floor.name))) {
            floors.add(floor);
        } else throw new IllegalStateException("A floor called "+floor.name+" is already defined");
    }

    public Hospital(List<HospitalRoom> floors) {
        this.floors = floors;
    }

    @Override
    public void onSave() {
        System.out.println("Saved hospital "+name);
    }

    @Override
    public void setPatients(Patient[] toArray) {
        throw new UnsupportedOperationException();
    }

    public Hospital() {

    }

}
