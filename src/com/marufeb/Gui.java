package com.marufeb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

public class Gui extends JFrame {
    private final JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
    private BackupExecutor executor;
    private final JMenuBar menuBar = new JMenuBar();
    private final JPanel settings = new JPanel(new BorderLayout());
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
        final JPanel buttons = new JPanel(new BorderLayout());
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

        fields.add(label, BorderLayout.NORTH);

        buttons.add(save, BorderLayout.WEST);
        buttons.add(cancel, BorderLayout.EAST);

        users.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        settings.add(fields, BorderLayout.NORTH);
        settings.add(users, BorderLayout.CENTER);
        settings.add(buttons, BorderLayout.SOUTH);
        update(settings);
    }

    /**
     * Creates the new patient window and allows the end user to process the user insertion.
     * It also update the graphics.
     */
    private void createPatient() {

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
