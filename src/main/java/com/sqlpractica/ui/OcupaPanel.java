package com.sqlpractica.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.sqlpractica.DAOException;
import com.sqlpractica.dao.OcupaDAO;
import com.sqlpractica.model.Ocupa;

/**
 * Panel CRUD para la tabla 'ocupa'.
 *
 * NOVEDAD: la PK es COMPUESTA (NSS + código plaza). Por eso, para
 * editar o eliminar, necesitamos las DOS claves identificando la fila.
 *
 * Las fechas se introducen como texto en formato yyyy-MM-dd.
 * Para validar que el usuario escribe una fecha real, usamos
 * LocalDate.parse(): si el formato es incorrecto lanza una
 * DateTimeParseException, que capturamos y convertimos en un mensaje
 * claro al usuario.
 *
 * Doc LocalDate.parse:
 *   https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html#parse(java.lang.CharSequence)
 */
public class OcupaPanel extends JPanel {

    private final OcupaDAO dao = new OcupaDAO();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[] {"NSS empleado", "Código plaza", "Fecha inicio", "Fecha fin"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private final JTable tabla = new JTable(modelo);

    private final JTextField tfNss = new JTextField();
    private final JTextField tfPlaza = new JTextField();
    private final JTextField tfFechaInicio = new JTextField();
    private final JTextField tfFechaFin = new JTextField();

    public OcupaPanel() {
        setLayout(new BorderLayout(8, 8));

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel formulario = new JPanel(new GridLayout(5, 2, 4, 4));
        formulario.add(new JLabel("NSS empleado:"));
        formulario.add(tfNss);
        formulario.add(new JLabel("Código plaza:"));
        formulario.add(tfPlaza);
        formulario.add(new JLabel("Fecha inicio (yyyy-MM-dd):"));
        formulario.add(tfFechaInicio);
        formulario.add(new JLabel("Fecha fin (opcional):"));
        formulario.add(tfFechaFin);

        JButton btCrear = new JButton("Crear");
        JButton btEditar = new JButton("Editar");
        JButton btEliminar = new JButton("Eliminar");
        JButton btLimpiar = new JButton("Limpiar");

        btCrear.addActionListener(e -> crear());
        btEditar.addActionListener(e -> editar());
        btEliminar.addActionListener(e -> eliminar());
        btLimpiar.addActionListener(e -> limpiar());

        JPanel botones = new JPanel(new GridLayout(1, 4, 4, 4));
        botones.add(btCrear);
        botones.add(btEditar);
        botones.add(btEliminar);
        botones.add(btLimpiar);
        formulario.add(botones);

        add(formulario, BorderLayout.SOUTH);

        recargar();
    }

    private void recargar() {
        try {
            modelo.setRowCount(0);
            List<Ocupa> lista = dao.obtenerTodos();
            for (Ocupa o : lista) {
                String fin = o.getFechaFin();
                modelo.addRow(new Object[] {
                        o.getNssEmpleado(),
                        o.getCodigoPlaza(),
                        o.getFechaInicio(),
                        fin == null ? "" : fin
                });
            }
        } catch (DAOException ex) {
            error(ex.getMessage());
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        tfNss.setText(textoCelda(fila, 0));
        tfPlaza.setText(textoCelda(fila, 1));
        tfFechaInicio.setText(textoCelda(fila, 2));
        tfFechaFin.setText(textoCelda(fila, 3));
    }

    /**
     * Construye un Ocupa a partir del formulario.
     * Valida los campos obligatorios y el formato de las fechas.
     */
    private Ocupa leerFormulario() {
        if (tfNss.getText().isBlank()) {
            throw new RuntimeException("Falta el NSS del empleado.");
        }
        if (tfPlaza.getText().isBlank()) {
            throw new RuntimeException("Falta el código de la plaza.");
        }
        if (tfFechaInicio.getText().isBlank()) {
            throw new RuntimeException("Falta la fecha de inicio.");
        }

        // Validamos formato. fechaFin solo se valida si se ha rellenado.
        validarFecha(tfFechaInicio.getText().trim());
        if (!tfFechaFin.getText().isBlank()) {
            validarFecha(tfFechaFin.getText().trim());
        }

        String fin = tfFechaFin.getText().trim();
        return new Ocupa(
                tfNss.getText().trim(),
                tfPlaza.getText().trim(),
                tfFechaInicio.getText().trim(),
                fin.isEmpty() ? null : fin);
    }

    /**
     * Comprueba que el texto sea una fecha válida en formato ISO
     * (yyyy-MM-dd). Si no lo es, LocalDate.parse lanza una excepción
     * que convertimos en un mensaje legible.
     */
    private void validarFecha(String texto) {
        try {
            LocalDate.parse(texto);
        } catch (DateTimeParseException ex) {
            throw new RuntimeException("Fecha incorrecta: " + texto + " (usa yyyy-MM-dd).");
        }
    }

    private void crear() {
        try {
            dao.insertar(leerFormulario());
            recargar();
            limpiar();
        } catch (DAOException ex) {
            // Suele ser FK fallida: NSS o codigo_plaza no existen.
            error(ex.getMessage());
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    /**
     * Para editar hacen falta las DOS partes de la PK (NSS + plaza),
     * por eso comprobamos las dos antes.
     */
    private void editar() {
        if (tfNss.getText().isBlank() || tfPlaza.getText().isBlank()) {
            error("Selecciona una ocupación.");
            return;
        }
        try {
            dao.actualizar(leerFormulario());
            recargar();
        } catch (DAOException ex) {
            error(ex.getMessage());
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    private void eliminar() {
        if (tfNss.getText().isBlank() || tfPlaza.getText().isBlank()) {
            error("Selecciona una ocupación.");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar esta ocupación?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.eliminar(tfNss.getText().trim(), tfPlaza.getText().trim());
            recargar();
            limpiar();
        } catch (DAOException ex) {
            error(ex.getMessage());
        }
    }

    private void limpiar() {
        tfNss.setText("");
        tfPlaza.setText("");
        tfFechaInicio.setText("");
        tfFechaFin.setText("");
        tabla.clearSelection();
    }

    private void error(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private String textoCelda(int fila, int columna) {
        Object valor = modelo.getValueAt(fila, columna);
        if (valor == null) {
            return "";
        }
        return valor.toString();
    }
}
