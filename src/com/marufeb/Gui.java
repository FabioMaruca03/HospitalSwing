package com.marufeb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Gui extends JFrame {
    private final JPanel hView = new JPanel(new BorderLayout());
    private final DefaultListModel<String> patientsData = new DefaultListModel<>();
    private final JFileChooser picker = new JFileChooser(System.getProperty("user.home"));
    private Backup backup;
    private Hospital h;
    private Component view = null;
    private File opened;
    private final JPanel person = new JPanel(new BorderLayout());

    private void load() {
        try {
            if (JOptionPane.showConfirmDialog(this, "Do you want to load a file?\nIf you want a new Hospital close the window") == JOptionPane.OK_OPTION) {
                picker.setSelectedFile(opened);
                picker.showOpenDialog(this);
                final File selectedFile = picker.getSelectedFile();
                h = Hospital.load(selectedFile);
                for (Patient patient : h.getFloors().get(0).getPatientsList()) {
                    patientsData.addElement(patient.getName());
                }
                opened = selectedFile;
            } else {
                h = new Hospital();
                h.addFloor(new HospitalRoom(new ArrayList<>()));
                opened = new File(System.getProperty("user.home")+File.separatorChar+"Hospital Management System.bak");
            }
            setTitle("Hospital "+h.getName());
            if (backup != null) backup.join();
            backup = new Backup(h);
            showHospitalInit();
        } catch (Exception e) {
            popup(e);
            load();
        }
    }

    private void showHospitalInit() {

        final JPanel panel = new JPanel(new FlowLayout());
        final Button save = new Button("Save");
        final JList<String> users = new JList<>(patientsData);
        final JPanel people = new JPanel(new BorderLayout());
        final JTextField hName = new JTextField(h.getName());
        final JPanel fields = new JPanel(new BorderLayout());
        final Button cancel = new Button("Close");
        final Button create = new Button("New patient");
        final JPanel label = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        final JPanel buttons = new JPanel(new BorderLayout());
        final Button delete = new Button("Delete patient");


        cancel.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        label.add(new JLabel("Hospital"));
        label.add(hName);

        delete.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String temp = users.getSelectedValue();
                if (temp != null) {
                    users.clearSelection();
                    patientsData.removeElement(temp);
                    h.getFloors().get(0).setPatients(Arrays.stream(h.getFloors().get(0).getPatientsList()).filter(it->!it.getName().equals(temp)).toArray(Patient[]::new));
                }
            }
        });

        save.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (h == null) {
                        h = new Hospital();
                    }
                    h.setName(hName.getText());
                    setTitle("Hospital ".concat(h.getName()));
                    if (picker.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                        Hospital.save(h, picker.getSelectedFile());
                    } else throw new IllegalStateException("Cannot save the file");
                } catch (Exception exception) {
                    popup(exception);
                }
            }
        });


        create.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePatient();
            }
        });

        panel.add(delete);
        panel.add(create);
        fields.add(label, BorderLayout.NORTH);
        people.add(users, BorderLayout.CENTER);
        people.add(panel, BorderLayout.SOUTH);
        hView.add(fields, BorderLayout.NORTH);
        hView.add(people, BorderLayout.CENTER);
        hView.add(buttons, BorderLayout.SOUTH);
        buttons.add(save, BorderLayout.WEST);
        buttons.add(cancel, BorderLayout.EAST);

        users.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pack();
        changeView(hView);
    }

    public Gui() throws HeadlessException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        picker.setName("Load hospital");
        setSize(new Dimension(700, 500));
        setTitle("Hospital: ");
        load();
        setVisible(true);
    }

    private void generatePatient() {
        person.removeAll();
        final JButton cancel = new JButton("Cancel");
        final JPanel data = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JTextField name = new JTextField("Name");
        final JTextField age = new JTextField("Age");
        final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JButton save = new JButton("Save");
        final JTextArea illness = new JTextArea("Description");


        cancel.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeView(hView);
            }
        });

        save.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final Patient patient = new Patient(name.getText(), Integer.parseInt(age.getText()), illness.getText(), new Date(System.currentTimeMillis()));
                    patientsData.addElement(patient.getName());
                    h.getFloors().get(0).addPatient(patient);
                } catch (Exception ex) { popup(ex); }
                changeView(hView);
            }
        });
        person.add(data, BorderLayout.CENTER);
        person.add(buttons, BorderLayout.SOUTH);


        buttons.add(save);
        buttons.add(cancel);

        data.add(name);
        data.add(age);
        data.add(illness);

        pack();
        changeView(person);
    }

    private void changeView(Component view) {
        if (this.view != null)
            getContentPane().remove(this.view);
        if (view != null) {
            getContentPane().add(view, BorderLayout.CENTER);
            this.view = view;
        }
    }

    private void popup(Exception e) {
        JOptionPane.showMessageDialog(this, new JLabel(e.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        new Gui();
    }
}
