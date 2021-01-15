package com.marufeb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Gui extends JFrame {
    private Backup thread;
    private final JPanel jPanel = new JPanel(new BorderLayout());
    private Component c = null;
    private File file;
    private Hospital basement;
    private final JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.home"));
    private final DefaultListModel<String> simpleData = new DefaultListModel<>();
    private final JPanel hospital = new JPanel(new BorderLayout());

    private void whenErrorAppears(Exception e) {
        JOptionPane.showMessageDialog(this, new JLabel(e.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void init() {

        final JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        final Button button2 = new Button("New patient");
        final JPanel jPanel4 = new JPanel(new BorderLayout());
        final JPanel jPanel3 = new JPanel(new BorderLayout());
        final Button button3 = new Button("Delete patient");
        final JPanel jPanel1 = new JPanel(new BorderLayout());
        final Button button = new Button("Save");
        final JTextField jTextField = new JTextField(basement.getName());
        final Button button1 = new Button("Close");
        final JList<String> stringJList = new JList<>(simpleData);
        final JPanel jPanel2 = new JPanel(new FlowLayout());


        panel1.add(jTextField);


        panel1.add(new JLabel("Hospital"));

        button2.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getPatient();
            }
        });

        button3.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String temp = stringJList.getSelectedValue();
                if (temp != null) {
                    stringJList.clearSelection();
                    simpleData.removeElement(temp);
                    basement.getFloors().get(0).setPatients(Arrays.stream(basement.getFloors().get(0).getPatientsList()).filter(it->!it.getName().equals(temp)).toArray(Patient[]::new));
                }
            }
        });

        button1.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (basement == null) {
                        basement = new Hospital();
                    }
                    setTitle("Hospital ".concat(basement.getName()));
                    basement.setName(jTextField.getText());

                    if (jFileChooser.showSaveDialog(c) == JFileChooser.APPROVE_OPTION) {
                        Hospital.save(basement, jFileChooser.getSelectedFile());
                    } else throw new IllegalStateException("Cannot save the file");
                } catch (Exception exception) {
                    whenErrorAppears(exception);
                }
            }
        });




        hospital.add(jPanel3, BorderLayout.SOUTH);
        jPanel3.add(button1, BorderLayout.EAST);


        jPanel2.add(button3);
        jPanel1.add(jPanel2, BorderLayout.SOUTH);
        jPanel4.add(panel1, BorderLayout.NORTH);

        jPanel3.add(button, BorderLayout.WEST);
        stringJList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jPanel2.add(button2);

        jPanel1.add(stringJList, BorderLayout.CENTER);
        hospital.add(jPanel1, BorderLayout.CENTER);
        hospital.add(jPanel4, BorderLayout.NORTH);

        pack();
        uploadView(hospital);
    }

    private void get() {
        try {
            if (JOptionPane.showConfirmDialog(this, "Do you want to load a file?\nIf you want a new Hospital close the window") == JOptionPane.OK_OPTION) {
                jFileChooser.setSelectedFile(file);
                jFileChooser.showOpenDialog(this);
                final File selectedFile = jFileChooser.getSelectedFile();
                basement = Hospital.load(selectedFile);
                for (Patient patient : basement.getFloors().get(0).getPatientsList()) {
                    simpleData.addElement(patient.getName());
                }
                file = selectedFile;
            } else {
                basement = new Hospital();
                basement.addFloor(new HospitalRoom(new ArrayList<>()));
                file = new File(System.getProperty("user.home")+File.separatorChar+"Hospital Management System.bak");
            }
            setTitle("Hospital "+ basement.getName());
            if (thread != null) thread.join();
            thread = new Backup(basement);
            init();
        } catch (Exception e) {
            whenErrorAppears(e);
            get();
        }
    }

    public Gui() throws HeadlessException {
        setSize(new Dimension(700, 500));
        setLayout(new BorderLayout());
        jFileChooser.setName("Load hospital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Hospital: ");
        get();
        setVisible(true);
    }

    private void uploadView(Component c) {
        if (this.c != null)
            getContentPane().remove(this.c);
        if (c != null) {
            getContentPane().add(c, BorderLayout.CENTER);
            this.c = c;
        }
    }

    private void getPatient() {
        jPanel.removeAll();
        final JButton jButton1 = new JButton("Save");
        final JPanel jPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JTextField jTextField = new JTextField("Age");

        final JTextArea description = new JTextArea("Description");
        final JButton jButton = new JButton("Cancel");
        final JPanel jPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));

        final JTextField jTextField1 = new JTextField("Name");

        jButton1.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final Patient patient = new Patient(jTextField1.getText(), Integer.parseInt(jTextField.getText()), description.getText(), new Date(System.currentTimeMillis()));
                    simpleData.addElement(patient.getName());
                    basement.getFloors().get(0).addPatient(patient);
                } catch (Exception ex) { whenErrorAppears(ex); }
                uploadView(hospital);
            }
        });
        jButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadView(hospital);
            }
        });


        jPanel2.add(jTextField1);
        jPanel2.add(jTextField);
        jPanel.add(jPanel2, BorderLayout.CENTER);
        jPanel1.add(jButton);
        jPanel2.add(description);
        jPanel1.add(jButton1);

        jPanel.add(jPanel1, BorderLayout.SOUTH);

        pack();
        uploadView(jPanel);
    }

    public static void main(String[] args) {
        new Gui();
    }
}
