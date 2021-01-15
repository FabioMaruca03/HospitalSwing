package com.marufeb;

import java.util.*;

public class HospitalRoom extends HospitalFloor {
    private final Map<UUID, Patient> patients = new HashMap<>();

    public HospitalRoom(List<Patient> patients) {
        super();
        patients.forEach(p-> this.patients.putIfAbsent(p.getId(), p));
    }

    public Map<UUID, Patient> getPatients() {
        return patients;
    }

    public Patient[] getPatientsList() {
        return patients.values().toArray(new Patient[0]);
    }

    public void addPatient(Patient patient) {
        if (patient.getRoom() == null) {
            patients.putIfAbsent(patient.getId(), patient);
            patient.setRoom(this);
        } else throw new IllegalStateException("Patient is already in room: "+patient.getRoom().name);
    }

    private void remPatient(UUID patient) {
        patients.remove(patient);
    }

    public void remPatient(Patient patient) {
        if (patient.getRoom().equals(this)) {
            remPatient(patient.getId());
            patient.setRoom(null);
        } else throw new IllegalStateException("Patient "+patient.getId().toString()+" not in room "+name);
    }

    @Override
    public void onLoad() {
        System.out.println("Loaded floor: ".concat(name));
    }

    @Override
    public void onSave() {
        System.out.println("Saving floor: ".concat(name));
    }

    @Override
    public void setPatients(Patient[] toArray) {
        patients.clear();
        Arrays.stream(toArray).forEach(it->patients.putIfAbsent(it.getId(), it));
    }
}
