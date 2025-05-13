package org.example;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaUpdate extends JFrame {
    private JLabel lblTitulo, lblNombre, lblApellidoP, lblApellidoM, lblCurp, lblFechaNac, lblGenero, lblEstado;
    private JTextField txtNombre, txtApellidoPaterno, txtApellidoMaterno, txtCurp, txtFechaNac;
    private JRadioButton rbFemenino, rbMasculino;
    private ButtonGroup bgGenero;
    private JComboBox<String> cbEstado;
    private JButton btnRegresar, btnActualizar;
    private JPanel panelPrincipal;

    private Persona persona;

    public VentanaUpdate() {
        setTitle("Actualizar CURP");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        persona = new Persona();
        inicializarComponentes();
        agregarComponentes();
        configurarEventos();
    }

    private void inicializarComponentes() {
        lblTitulo = new JLabel("Actualizar Datos por CURP", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));

        lblNombre = new JLabel("Nombre(s): ", JLabel.RIGHT);
        lblApellidoP = new JLabel("Apellido paterno: ", JLabel.RIGHT);
        lblApellidoM = new JLabel("Apellido materno: ", JLabel.RIGHT);
        lblCurp = new JLabel("CURP: ", JLabel.RIGHT);
        lblFechaNac = new JLabel("Fecha de Nacimiento: ", JLabel.RIGHT);
        lblGenero = new JLabel("Género: ", JLabel.RIGHT);
        lblEstado = new JLabel("Estado: ", JLabel.RIGHT);

        txtNombre = new JTextField();
        txtApellidoPaterno = new JTextField();
        txtApellidoMaterno = new JTextField();
        txtCurp = new JTextField(18);
        txtFechaNac = new JTextField();
        txtFechaNac.setEditable(false);

        rbFemenino = new JRadioButton("Femenino");
        rbMasculino = new JRadioButton("Masculino");

        bgGenero = new ButtonGroup();
        bgGenero.add(rbFemenino);
        bgGenero.add(rbMasculino);

        cbEstado = new JComboBox<>();
        cbEstado.addItem("Selecciona un estado");
        for (String estado : Persona.getEstadosMap().values()) {
            cbEstado.addItem(estado);
        }

        btnRegresar = new JButton("Regresar");
        btnActualizar = new JButton("Actualizar");

        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void agregarComponentes() {
        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));

        panelFormulario.add(lblCurp);
        panelFormulario.add(txtCurp);
        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        txtNombre.setEditable(false);
        panelFormulario.add(lblApellidoP);
        panelFormulario.add(txtApellidoPaterno);
        panelFormulario.add(lblApellidoM);
        panelFormulario.add(txtApellidoMaterno);
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
        panelBoton.add(btnActualizar);

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void configurarEventos() {
        txtCurp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                cargarDatos();
            }
        });

        btnRegresar.addActionListener(e -> {
            new VentanaEleccion().setVisible(true);
            dispose();
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

        btnActualizar.addActionListener(e -> actualizarDatos());
    }

    private void cargarDatos() {
        String curp = txtCurp.getText().trim();
        if (curp.length() != 18) {
            JOptionPane.showMessageDialog(this, "El CURP debe tener 18 caracteres", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        persona = PersonaDAO.buscarPorCURP(curp);
        if (persona != null) {
            txtNombre.setText(persona.getNombre());
            txtApellidoPaterno.setText(persona.getApellidoPaterno());
            txtApellidoMaterno.setText(persona.getApellidoMaterno());
            txtFechaNac.setText(persona.getFechaNac().toString());

            if (persona.getGenero().equalsIgnoreCase("Femenino")) {
                rbFemenino.setSelected(true);
            } else {
                rbMasculino.setSelected(true);
            }

            cbEstado.setSelectedItem(persona.getEstado());
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró un registro con ese CURP.", "No encontrado", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        }
    }

    private void actualizarDatos() {
        if (persona == null || persona.getCurp() == null) {
            JOptionPane.showMessageDialog(this, "Busca un CURP válido antes de actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombre = txtNombre.getText().trim();
        String apellidoP = txtApellidoPaterno.getText().trim();
        String apellidoM = txtApellidoMaterno.getText().trim();

        if (nombre.isEmpty() || apellidoP.isEmpty() || apellidoM.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos de nombre y apellidos deben estar llenos.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        persona.setNombre(nombre);
        persona.setApellidoPaterno(apellidoP);
        persona.setApellidoMaterno(apellidoM);

        boolean exito = PersonaDAO.actualizarSoloNombres(persona);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        txtFechaNac.setText("");
        rbFemenino.setSelected(false);
        rbMasculino.setSelected(false);
        cbEstado.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new VentanaUpdate().setVisible(true));
    }
}
