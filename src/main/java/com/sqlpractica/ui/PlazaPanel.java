package com.sqlpractica.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import com.sqlpractica.dao.PlazaDAO;
import com.sqlpractica.model.Plaza;

/**
 * Panel CRUD para la tabla 'plaza'.
 *
 * NOVEDADES respecto a EmpleadoPanel:
 *   - Hay un campo numérico (salario): hay que parsearlo a double, y
 *     si el usuario escribe algo no numérico avisamos.
 *   - Hay 2 campos opcionales (supervisora e informe): si vienen
 *     vacíos los pasamos como null al objeto Plaza.
 *   - leerFormulario() centraliza la validación y la conversión de
 *     campos -> objeto Plaza, así crear() y editar() comparten lógica.
 */
public class PlazaPanel extends JPanel {

    private final PlazaDAO dao = new PlazaDAO();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[] {"Código", "Nombre", "Salario", "Supervisora", "Informe", "Tipo"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private final JTable tabla = new JTable(modelo);

    private final JTextField tfCodigo = new JTextField();
    private final JTextField tfNombre = new JTextField();
    private final JTextField tfSalario = new JTextField();
    private final JTextField tfSupervisora = new JTextField();
    private final JTextField tfInforme = new JTextField();
    private final JTextField tfTipo = new JTextField();

    public PlazaPanel() {
        setLayout(new BorderLayout(8, 8));

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // 6 filas de campos + 1 fila de botones = 7.
        JPanel formulario = new JPanel(new GridLayout(7, 2, 4, 4));
        formulario.add(new JLabel("Código:"));
        formulario.add(tfCodigo);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(tfNombre);
        formulario.add(new JLabel("Salario:"));
        formulario.add(tfSalario);
        formulario.add(new JLabel("Cód. supervisora:"));
        formulario.add(tfSupervisora);
        formulario.add(new JLabel("Informe supervisión:"));
        formulario.add(tfInforme);
        formulario.add(new JLabel("Tipo plaza:"));
        formulario.add(tfTipo);

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
            List<Plaza> lista = dao.obtenerTodos();
            for (Plaza p : lista) {
                // Si supervisora o informe son null, mostramos "" en la
                // tabla en vez de la palabra "null".
                String supervisora = p.getCodigoPlazaSupervisora();
                String informe = p.getInformeSupervision();

                modelo.addRow(new Object[] {
                    p.getCodigo(),
                    p.getNombre(),
                    p.getSalario(),
                    supervisora == null ? "" : supervisora,
                    informe == null ? "" : informe,
                    p.getNombreTipoPlaza()
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
        tfCodigo.setText(textoCelda(fila, 0));
        tfNombre.setText(textoCelda(fila, 1));
        tfSalario.setText(textoCelda(fila, 2));
        tfSupervisora.setText(textoCelda(fila, 3));
        tfInforme.setText(textoCelda(fila, 4));
        tfTipo.setText(textoCelda(fila, 5));
    }

    /**
     * Lee los campos del formulario, los valida y construye un objeto Plaza.
     * Si algún campo obligatorio está vacío o el salario no es un número,
     * lanza RuntimeException con un mensaje claro (que crear/editar
     * convertirán en un diálogo de error).
     */
    private Plaza leerFormulario() {
        if (tfCodigo.getText().isBlank()) {
            throw new RuntimeException("El código es obligatorio.");
        }
        if (tfNombre.getText().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio.");
        }
        if (tfTipo.getText().isBlank()) {
            throw new RuntimeException("El tipo de plaza es obligatorio.");
        }

        // Salario: convertir el texto a double.
        // Aceptamos coma o punto decimal -> reemplazamos coma por punto.
        double salario;
        try {
            salario = Double.parseDouble(tfSalario.getText().trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            throw new RuntimeException("El salario tiene que ser un número.");
        }

        // Campos opcionales: si están vacíos, null para que la BD guarde NULL.
        String supervisora = tfSupervisora.getText().trim();
        String informe = tfInforme.getText().trim();

        return new Plaza(
                tfCodigo.getText().trim(),
                tfNombre.getText().trim(),
                salario,
                supervisora.isEmpty() ? null : supervisora,
                informe.isEmpty() ? null : informe,
                tfTipo.getText().trim());
    }

    private void crear() {
        try {
            dao.insertar(leerFormulario());
            recargar();
            limpiar();
        } catch (DAOException ex) {
            // Error proveniente de la BD (p.ej. tipo_plaza no existe).
            error(ex.getMessage());
        } catch (RuntimeException ex) {
            // Error de validación lanzado por leerFormulario().
            error(ex.getMessage());
        }
    }

    private void editar() {
        if (tfCodigo.getText().isBlank()) {
            error("Selecciona una plaza.");
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
        if (tfCodigo.getText().isBlank()) {
            error("Selecciona una plaza.");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar la plaza '" + tfCodigo.getText() + "'?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.eliminar(tfCodigo.getText().trim());
            recargar();
            limpiar();
        } catch (DAOException ex) {
            error(ex.getMessage());
        }
    }

    private void limpiar() {
        tfCodigo.setText("");
        tfNombre.setText("");
        tfSalario.setText("");
        tfSupervisora.setText("");
        tfInforme.setText("");
        tfTipo.setText("");
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
