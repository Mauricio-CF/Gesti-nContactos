package vista;

import javax.swing.*;
import javax.swing.table.*;
import controlador.LogicaContactos;
import modelo.Persona;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class VentanaPrincipal extends JFrame {
    private JTextField txtNombre, txtTelefono, txtEmail, txtBuscar;
    private JComboBox<String> cmbCategoria;
    private JCheckBox chkFavorito;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private LogicaContactos logica;
    private JProgressBar progressBar;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                VentanaPrincipal frame = new VentanaPrincipal();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public VentanaPrincipal() {
        setTitle("Gestión de Contactos - Mejorada");
        setBounds(100, 100, 900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        logica = new LogicaContactos();
        try {
            logica.cargarDesdeArchivo();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar contactos.");
        }

        JTabbedPane tabs = new JTabbedPane();
        getContentPane().add(tabs, BorderLayout.CENTER);

        JPanel panelContactos = new JPanel(null);
        tabs.addTab("Contactos", panelContactos);

        JPanel panelEstadisticas = new JPanel();
        panelEstadisticas.add(new JLabel("Estadísticas próximamente..."));
        tabs.addTab("Estadísticas", panelEstadisticas);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(30, 20, 100, 20);
        panelContactos.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(130, 20, 200, 20);
        panelContactos.add(txtNombre);

        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setBounds(30, 50, 100, 20);
        panelContactos.add(lblTelefono);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(130, 50, 200, 20);
        panelContactos.add(txtTelefono);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(30, 80, 100, 20);
        panelContactos.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(130, 80, 200, 20);
        panelContactos.add(txtEmail);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setBounds(30, 110, 100, 20);
        panelContactos.add(lblCategoria);

        cmbCategoria = new JComboBox<>(new String[]{"Trabajo", "Familia", "Amigos", "Otros"});
        cmbCategoria.setBounds(130, 110, 200, 20);
        panelContactos.add(cmbCategoria);

        chkFavorito = new JCheckBox("Favorito");
        chkFavorito.setBounds(130, 140, 100, 20);
        panelContactos.add(chkFavorito);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(360, 20, 120, 25);
        panelContactos.add(btnAgregar);

        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBounds(360, 50, 120, 25);
        panelContactos.add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(360, 80, 120, 25);
        panelContactos.add(btnEliminar);

        JButton btnExportar = new JButton("Exportar CSV");
        btnExportar.setBounds(360, 110, 120, 25);
        panelContactos.add(btnExportar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(520, 20, 200, 25);
        panelContactos.add(txtBuscar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(730, 20, 100, 25);
        panelContactos.add(btnBuscar);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 180, 800, 300);
        panelContactos.add(scrollPane);

        modeloTabla = new DefaultTableModel(new Object[]{"Nombre", "Teléfono", "Email", "Categoría", "Favorito"}, 0);
        tabla = new JTable(modeloTabla);
        tabla.setAutoCreateRowSorter(true);
        scrollPane.setViewportView(tabla);

        progressBar = new JProgressBar();
        progressBar.setBounds(30, 500, 800, 20);
        progressBar.setVisible(false);
        progressBar.setStringPainted(true);
        panelContactos.add(progressBar);

        cargarTabla();

        // AGREGAR
        btnAgregar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            String categoria = (String) cmbCategoria.getSelectedItem();
            boolean favorito = chkFavorito.isSelected();

            if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            Persona p = new Persona(nombre, telefono, email, categoria, favorito);
            try {
                logica.agregarContacto(p);
                modeloTabla.addRow(new Object[]{nombre, telefono, email, categoria, favorito});
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Contacto agregado.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar.");
            }
        });

        // MODIFICAR
        btnModificar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                String nombre = txtNombre.getText().trim();
                String telefono = txtTelefono.getText().trim();
                String email = txtEmail.getText().trim();
                String categoria = (String) cmbCategoria.getSelectedItem();
                boolean favorito = chkFavorito.isSelected();

                if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                    return;
                }

                Persona p = new Persona(nombre, telefono, email, categoria, favorito);
                try {
                    logica.modificarContacto(fila, p);
                    modeloTabla.setValueAt(nombre, fila, 0);
                    modeloTabla.setValueAt(telefono, fila, 1);
                    modeloTabla.setValueAt(email, fila, 2);
                    modeloTabla.setValueAt(categoria, fila, 3);
                    modeloTabla.setValueAt(favorito, fila, 4);
                    JOptionPane.showMessageDialog(this, "Contacto modificado.");
                    limpiarCampos();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al modificar.");
                }
            }
        });

        // ELIMINAR
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                try {
                    logica.eliminarContacto(fila);
                    modeloTabla.removeRow(fila);
                    JOptionPane.showMessageDialog(this, "Contacto eliminado.");
                    limpiarCampos();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar.");
                }
            }
        });

        // BUSCAR
        btnBuscar.addActionListener(e -> {
            String filtro = txtBuscar.getText().toLowerCase().trim();
            modeloTabla.setRowCount(0);
            for (Persona p : logica.getContactos()) {
                if (p.getNombre().toLowerCase().contains(filtro)) {
                    modeloTabla.addRow(new Object[]{
                        p.getNombre(), p.getTelefono(), p.getEmail(), p.getCategoria(), p.isFavorito()
                    });
                }
            }
        });

        // EXPORTAR
        btnExportar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                progressBar.setVisible(true);
                progressBar.setIndeterminate(true);
                try {
                    logica.exportarCSV(f.getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "Exportación completada.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al exportar.");
                } finally {
                    progressBar.setIndeterminate(false);
                    progressBar.setVisible(false);
                }
            }
        });

        // SELECCIONAR FILA
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                txtNombre.setText(tabla.getValueAt(fila, 0).toString());
                txtTelefono.setText(tabla.getValueAt(fila, 1).toString());
                txtEmail.setText(tabla.getValueAt(fila, 2).toString());
                cmbCategoria.setSelectedItem(tabla.getValueAt(fila, 3).toString());
                chkFavorito.setSelected(Boolean.parseBoolean(tabla.getValueAt(fila, 4).toString()));
            }
        });

        // MENÚ CONTEXTUAL
        JPopupMenu menu = new JPopupMenu();
        JMenuItem miEliminar = new JMenuItem("Eliminar");
        JMenuItem miModificar = new JMenuItem("Modificar");
        menu.add(miModificar);
        menu.add(miEliminar);
        tabla.setComponentPopupMenu(menu);
        miEliminar.addActionListener(e -> btnEliminar.doClick());
        miModificar.addActionListener(e -> btnModificar.doClick());
    }

    private void cargarTabla() {
        for (Persona p : logica.getContactos()) {
            modeloTabla.addRow(new Object[]{
                p.getNombre(), p.getTelefono(), p.getEmail(), p.getCategoria(), p.isFavorito()
            });
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        cmbCategoria.setSelectedIndex(0);
        chkFavorito.setSelected(false);
        txtBuscar.setText("");
    }
}
