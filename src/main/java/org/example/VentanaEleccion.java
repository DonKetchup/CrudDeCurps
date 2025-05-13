package org.example;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class VentanaEleccion extends JFrame {
    private JButton btnCreate, btnRead, btnUpdate, btnDelete;

    public VentanaEleccion() {
        setTitle("Selecciona la opción que deseas");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        lanzarVentanas();
    }

    private void inicializarComponentes() {
        btnCreate = new JButton("Create");
        btnRead = new JButton("Read");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(btnCreate);
        panel.add(btnRead);
        panel.add(btnUpdate);
        panel.add(btnDelete);

        add(panel);
    }

    private void lanzarVentanas() {

        btnCreate.addActionListener(e -> {
            new VentanaCreate().setVisible(true);
            dispose();
        });

        btnRead.addActionListener(e -> {
            new VentanaRead().setVisible(true);
            dispose();
        });

        btnUpdate.addActionListener(e -> {
            new VentanaUpdate().setVisible(true);
            dispose();
        });

        btnDelete.addActionListener(e -> {
            new VentanaDelete().setVisible(true);
            dispose();
        });

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("No se pudo cargar FlatLaf");
        }

        SwingUtilities.invokeLater(() -> new VentanaEleccion().setVisible(true));
    }
}
