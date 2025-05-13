package org.example;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaDelete extends JFrame {
    private JLabel lblTitulo, lblNombre, lblApellidoP, lblApellidoM, lblCurp, lblFechaNac, lblGenero, lblEstado;
    private JTextField txtNombre, txtApellidoPaterno, txtApellidoMaterno, txtCurp, txtFechaNac;
    private JRadioButton rbFemenino, rbMasculino;
    private ButtonGroup bgGenero;
    private JComboBox<String> cbEstado;
    private JButton btnRegresar, btnEliminar;
    private JPanel panelPrincipal;

    private Persona persona;

    public VentanaDelete() {
        setTitle("Eliminar Persona por CURP");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        agregarComponentes();
        configurarEventos();
    }

    private void inicializarComponentes() {
        lblTitulo = new JLabel("Eliminar Registro", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));

        lblNombre = new JLabel("Nombre(s): ", JLabel.RIGHT);
        lblApellidoP = new JLabel("Apellido paterno: ", JLabel.RIGHT);
        lblApellidoM = new JLabel("Apellido materno: ", JLabel.RIGHT);
        lblCurp = new JLabel("CURP: ", JLabel.RIGHT);
        lblFechaNac = new JLabel("Fecha de Nacimiento: ", JLabel.RIGHT);
        lblGenero = new JLabel("Género: ", JLabel.RIGHT);
        lblEstado = new JLabel("Estado: ", JLabel.RIGHT);

        txtNombre = new JTextField(); txtNombre.setEditable(false);
        txtApellidoPaterno = new JTextField(); txtApellidoPaterno.setEditable(false);
        txtApellidoMaterno = new JTextField(); txtApellidoMaterno.setEditable(false);
        txtCurp = new JTextField(18);
        txtFechaNac = new JTextField(); txtFechaNac.setEditable(false);

        rbFemenino = new JRadioButton("Femenino"); rbFemenino.setEnabled(false);
        rbMasculino = new JRadioButton("Masculino"); rbMasculino.setEnabled(false);
        bgGenero = new ButtonGroup();
        bgGenero.add(rbFemenino); bgGenero.add(rbMasculino);

        cbEstado = new JComboBox<>();
        cbEstado.addItem("Selecciona un estado");
        for (String estado : Persona.getEstadosMap().values()) cbEstado.addItem(estado);
        cbEstado.setEnabled(false);

        btnRegresar = new JButton("Regresar");
        btnEliminar = new JButton("Eliminar");

        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void agregarComponentes() {
        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));

        panelFormulario.add(lblCurp); panelFormulario.add(txtCurp);
        panelFormulario.add(lblNombre); panelFormulario.add(txtNombre);
        panelFormulario.add(lblApellidoP); panelFormulario.add(txtApellidoPaterno);
        panelFormulario.add(lblApellidoM); panelFormulario.add(txtApellidoMaterno);
        panelFormulario.add(lblFechaNac); panelFormulario.add(txtFechaNac);
        panelFormulario.add(lblGenero);

        JPanel panelGenero = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelGenero.add(rbFemenino); panelGenero.add(rbMasculino);
        panelFormulario.add(panelGenero);

        panelFormulario.add(lblEstado); panelFormulario.add(cbEstado);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnRegresar);
        panelBoton.add(btnEliminar);

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void configurarEventos() {
        txtCurp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                buscarPersona();
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

        btnEliminar.addActionListener(e -> eliminarRegistro());
    }

    private void buscarPersona() {
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

            if ("Femenino".equalsIgnoreCase(persona.getGenero())) {
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

    private void eliminarRegistro() {
        if (persona == null || persona.getCurp() == null) {
            JOptionPane.showMessageDialog(this, "Debes buscar un CURP válido antes de eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de eliminar a:\n" + persona.getNombre() + " " + persona.getApellidoPaterno() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean exito = PersonaDAO.eliminarPersonaPorCURP(persona.getCurp());
            if (exito) {
                JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
        persona = null;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new VentanaDelete().setVisible(true));
    }
}
