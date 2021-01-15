package com.marufeb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Gui extends JFrame {
    private final JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
    private BackupExecutor executor;
    private final JPanel settings = new JPanel(new BorderLayout());
    private final JPanel personForm = new JPanel(new BorderLayout());
    private final DefaultListModel<String> usersData = new DefaultListModel<>();
    private Component current = null;
    private File def = null;
    private Hospital currentH;

    /**
     * The Gui for this assignment
     * @throws HeadlessException A default exception. {@link UnsupportedOperationException}
     */
    public Gui() throws HeadlessException {
        setLayout(new BorderLayout());
        setSize(new Dimension(700, 500));
        this.setTitle("Hospital");
        chooser.setName("Load hospital");
        setTitle("Hospital: ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        load();
        setVisible(true);
    }

    /**
     * Generates a new error message dialog through {@link JOptionPane}
     * @param e The exception which represent the error.
     */
    private void genError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, new JLabel(e.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Asks for loading existing Hospital or creating a new one.
     */
    private void load() {
        try {
            if (JOptionPane.showConfirmDialog(this, "Do you want to load a file?\nIf you want a new Hospital close the window") == JOptionPane.OK_OPTION) {
                chooser.showOpenDialog(this);
                final File selectedFile = chooser.getSelectedFile();
                currentH = Hospital.load(selectedFile);
                def = selectedFile;
            } else {
                currentH = new Hospital();
                currentH.addFloor(new HospitalRoom(new ArrayList<>()));
                def = new File(System.getProperty("user.home")+File.separatorChar+"Hospital Management System.bak");
            }
            setTitle("Hospital ".concat(currentH.getName()));
            if (executor != null) executor.join();
            executor = new BackupExecutor(currentH);
            showHospitalInit();
        } catch (Exception e) {
            genError(e);
            load();
        }
    }

    /**
     * Initializes a new {@link Hospital} window
     */
    private void showHospitalInit() {
        final JTextField hName = new JTextField(currentH.getName());
        final JPanel fields = new JPanel(new BorderLayout());

        final Button save = new Button("Save");
        final Button cancel = new Button("Close");
        final Button create = new Button("New patient");
        final Button delete = new Button("Delete patient");
        final JPanel buttons = new JPanel(new BorderLayout());
        final JPanel persons = new JPanel(new BorderLayout());
        final JPanel personsOPT = new JPanel(new FlowLayout());
        final JPanel label = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));

        final JList<String> users = new JList<>(usersData);

        cancel.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    executor.join();
                } catch (InterruptedException ignore) { }
                System.exit(0);
            }
        });

        save.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (currentH == null) {
                        currentH = new Hospital();
                    }
                    currentH.setName(hName.getText());
                    setTitle("Hospital ".concat(currentH.getName()));
                    if (chooser.showSaveDialog(current) == JFileChooser.APPROVE_OPTION) {
                        Hospital.save(currentH, chooser.getSelectedFile());
                    } else throw new IllegalStateException("Cannot save the file");
                } catch (Exception exception) {
                    genError(exception);
                }
            }
        });

        label.add(new JLabel("Hospital name"));
        label.add(hName);

        delete.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String temp = users.getSelectedValue();
                if (temp != null) {
                    users.clearSelection();
                    usersData.removeElement(temp);
                    currentH.getFloors().get(0).setPatients(Arrays.stream(currentH.getFloors().get(0).getPatientsList()).filter(it->!it.getName().equals(temp)).toArray(Patient[]::new));
                }
            }
        });


        create.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPatient();
            }
        });

        personsOPT.add(delete);
        personsOPT.add(create);

        persons.add(users, BorderLayout.CENTER);
        persons.add(personsOPT, BorderLayout.SOUTH);

        fields.add(label, BorderLayout.NORTH);

        buttons.add(save, BorderLayout.WEST);
        buttons.add(cancel, BorderLayout.EAST);

        users.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        settings.add(fields, BorderLayout.NORTH);
        settings.add(persons, BorderLayout.CENTER);
        settings.add(buttons, BorderLayout.SOUTH);
        pack();
        update(settings);
    }

    /**
     * Creates the new patient window and allows the end user to process the user insertion.
     * It also update the graphics.
     */
    private void createPatient() {
        personForm.removeAll();
        final JTextField name = new JTextField("name");
        final JTextField age = new JTextField("age");
        final JTextArea illness = new JTextArea("Illness");
        final JButton save = new JButton("Save");
        final JButton cancel = new JButton("cancel");
        final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JPanel data = new JPanel(new FlowLayout(FlowLayout.CENTER));

        save.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final Patient patient = new Patient(name.getText(), Integer.parseInt(age.getText()), illness.getText(), new Date(System.currentTimeMillis()));
                    currentH.getFloors().get(0).addPatient(patient);
                    usersData.addElement(patient.getName());
                } catch (Exception ex) { genError(ex); }
                update(settings);
            }
        });

        cancel.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update(settings);
            }
        });

        data.add(name);
        data.add(age);
        data.add(illness);

        buttons.add(save);
        buttons.add(cancel);
        personForm.add(buttons, BorderLayout.SOUTH);
        personForm.add(data, BorderLayout.CENTER);
        pack();
        update(personForm);
    }

    /**
     * Updates the current showing component
     * @param component The component to update
     */
    private void update(Component component) {
        if (current != null)
            getContentPane().remove(current);
        if (component != null) {
            current = component;
            getContentPane().add(component, BorderLayout.CENTER);
        }
    }

    public static void main(String[] args) {
        new Gui();
    }
}
