package org.example;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaCreate extends JFrame {
    private JLabel lblTitulo, lblNombre, lblApellidoP, lblApellidoM, lblCurp, lblFechaNac, lblGenero, lblEstado;
    private JTextField txtNombre, txtApellidoPaterno, txtApellidoMaterno, txtCurp, txtFechaNac;
    private JRadioButton rbFemenino, rbMasculino;
    private ButtonGroup bgGenero;
    private JComboBox<String> cbEstado;
    private JButton btnSiguiente, btnRegresar;
    private JPanel panelPrincipal;

    private Persona persona;

    public VentanaCreate() {
        setTitle("Consulta CURP");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        persona = new Persona();
        inicializarComponentes();
        agregarComponentes();
        configurarEventos();
    }

    private void inicializarComponentes() {
        //Titulo
        lblTitulo = new JLabel("Registro de CURP", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));

        //Labels
        lblNombre = new JLabel("Nombre(s): ", JLabel.RIGHT);
        lblApellidoP = new JLabel("Apellido paterno: ", JLabel.RIGHT);
        lblApellidoM = new JLabel("Apellido materno: ", JLabel.RIGHT);
        lblCurp = new JLabel("CURP: ", JLabel.RIGHT);
        lblFechaNac = new JLabel("Fecha de Nacimiento: ", JLabel.RIGHT);
        lblGenero = new JLabel("Género: ", JLabel.RIGHT);
        lblEstado = new JLabel("Estado: ", JLabel.RIGHT);

        //Campos de los FIELD
        txtNombre = new JTextField();
        txtApellidoPaterno = new JTextField();
        txtApellidoMaterno = new JTextField();
        txtCurp = new JTextField(18);
        txtFechaNac = new JTextField(10);
        txtFechaNac.setEditable(false);

        //RadioButtons
        rbFemenino = new JRadioButton("Femenino");
        rbFemenino.setEnabled(false);
        rbMasculino = new JRadioButton("Masculino");
        rbMasculino.setEnabled(false);

        bgGenero = new ButtonGroup();
        bgGenero.add(rbFemenino);
        bgGenero.add(rbMasculino);

        cbEstado = new JComboBox<>();
        cbEstado.addItem("Selecciona un estado");
        for (String estado : Persona.getEstadosMap().values()) {
            cbEstado.addItem(estado);
        }
        cbEstado.setEnabled(false);

        btnRegresar = new JButton("Regresar");
        btnSiguiente = new JButton("Siguiente");

        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void agregarComponentes() {
        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblApellidoP);
        panelFormulario.add(txtApellidoPaterno);
        panelFormulario.add(lblApellidoM);
        panelFormulario.add(txtApellidoMaterno);
        panelFormulario.add(lblCurp);
        panelFormulario.add(txtCurp);
        panelFormulario.add(lblFechaNac);
        panelFormulario.add(txtFechaNac);
        panelFormulario.add(lblGenero);

        JPanel panelGenero = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelGenero.add(rbFemenino);
        panelGenero.add(rbMasculino);
        panelFormulario.add(panelGenero);

        panelFormulario.add(lblEstado);
        panelFormulario.add(cbEstado);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnRegresar);
        panelBoton.add(btnSiguiente);


        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void configurarEventos() {
        txtCurp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                procesarCURP();
            }
        });

        txtCurp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = txtCurp.getText();
                int pos = txtCurp.getCaretPosition();
                txtCurp.setText(texto.toUpperCase());
                txtCurp.setCaretPosition(Math.min(pos, texto.length()));
            }
        });

        txtCurp.addActionListener(e -> procesarCURP());

        btnSiguiente.addActionListener(e -> {
            if (txtCurp.getText().trim().length() == 18) {
                persona.setNombre(txtNombre.getText().trim());
                persona.setApellidoPaterno(txtApellidoPaterno.getText().trim());
                persona.setApellidoMaterno(txtApellidoMaterno.getText().trim());

                boolean exito = PersonaDAO.insertarPersona(persona);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Registro guardado exitosamente.");

                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Primero ingresa un CURP válido (18 caracteres)",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRegresar.addActionListener(e -> {
            new VentanaEleccion().setVisible(true);
            dispose(); // Cierra la ventana actual para no duplicar
        });
    }



    private void procesarCURP() {
        String curp = txtCurp.getText().trim();
        if (curp.length() == 18) {
            persona.setCurp(curp);
            txtFechaNac.setText(persona.getFechaNac().toString());

            if (persona.getGenero().equalsIgnoreCase("Femenino")) {
                rbFemenino.setSelected(true);
            } else {
                rbMasculino.setSelected(true);
            }

            cbEstado.setSelectedItem(persona.getEstado());
        } else if (!curp.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El CURP debe tener 18 caracteres",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar FlatLaf");
        }

        SwingUtilities.invokeLater(() -> new VentanaCreate().setVisible(true));
        Conexion.conectar();
    }
}
