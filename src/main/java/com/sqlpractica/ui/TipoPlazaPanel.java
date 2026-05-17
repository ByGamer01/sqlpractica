package com.sqlpractica.ui;

import com.sqlpractica.dao.TipoPlazaDAO;
import com.sqlpractica.model.TipoPlaza;
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

/**
 * Panel CRUD para la tabla 'tipo_plaza'
 * Mismo esquema que EmpleadoPanel, pero más sencillo: solo dos
 * columnas (nombre, funcion)
 *
 * NOTA: si se intenta eliminar un tipo que está siendo usado por una
 * plaza, la BD lo impedirá (ON DELETE RESTRICT) y se mostrará el error
 * que devuelva SQLite
 */
public class TipoPlazaPanel extends JPanel {

  private final TipoPlazaDAO dao = new TipoPlazaDAO();

  private final DefaultTableModel modelo =
      new DefaultTableModel(new String[] {"Nombre", "Función"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
          return false;
        }
      };

  private final JTable tabla = new JTable(modelo);

  private final JTextField tfNombre = new JTextField();
  private final JTextField tfFuncion = new JTextField();

  public TipoPlazaPanel() {
    setLayout(new BorderLayout(8, 8));

    // Selección por fila + listener para copiarla al formulario
    tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tabla.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        cargarSeleccion();
      }
    });

    add(new JScrollPane(tabla), BorderLayout.CENTER);

    // Formulario: 2 filas de campos + 1 fila de botones
    JPanel formulario = new JPanel(new GridLayout(3, 2, 4, 4));
    formulario.add(new JLabel("Nombre:"));
    formulario.add(tfNombre);
    formulario.add(new JLabel("Función:"));
    formulario.add(tfFuncion);

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

  /** Recarga la tabla pidiendo todos los tipos al DAO */
  private void recargar() {
    List<TipoPlaza> lista = dao.obtenerTodos();

    if (lista == null) {
      error(dao.getMensajeError());
      return;
    }

    modelo.setRowCount(0);

    for (TipoPlaza t : lista) {
      modelo.addRow(new Object[] {t.getNombre(), t.getFuncion()});
    }
  }

  /** Copia la fila seleccionada a los campos del formulario */
  private void cargarSeleccion() {
    int fila = tabla.getSelectedRow();

    if (fila < 0) {
      return;
    }

    tfNombre.setText(textoCelda(fila, 0));
    tfFuncion.setText(textoCelda(fila, 1));
  }

  private void crear() {
    if (tfNombre.getText().isBlank()) {
      error("El nombre es obligatorio.");
      return;
    }

    TipoPlaza nuevo =
        new TipoPlaza(tfNombre.getText().trim(), tfFuncion.getText().trim());

    if (!dao.insertar(nuevo)) {
      error(dao.getMensajeError());
      return;
    }

    recargar();
    limpiar();
  }

  /** El nombre es PK, por eso solo se actualiza la "funcion" */
  private void editar() {
    if (tfNombre.getText().isBlank()) {
      error("Selecciona un tipo de plaza.");
      return;
    }

    TipoPlaza modificado =
        new TipoPlaza(tfNombre.getText().trim(), tfFuncion.getText().trim());

    if (!dao.actualizar(modificado)) {
      error(dao.getMensajeError());
      return;
    }

    recargar();
  }

  private void eliminar() {
    if (tfNombre.getText().isBlank()) {
      error("Selecciona un tipo de plaza.");
      return;
    }

    int respuesta = JOptionPane.showConfirmDialog(
        this, "¿Eliminar el tipo '" + tfNombre.getText() + "'?", "Confirmar",
        JOptionPane.YES_NO_OPTION);

    if (respuesta != JOptionPane.YES_OPTION) {
      return;
    }
    // Aquí cae el "FOREIGN KEY constraint failed" si el tipo esta siendo usado
    // por alguna plaza
    if (!dao.eliminar(tfNombre.getText().trim())) {
      error(dao.getMensajeError());
      return;
    }

    recargar();
    limpiar();
  }

  private void limpiar() {
    tfNombre.setText("");
    tfFuncion.setText("");
    tabla.clearSelection();
  }

  private void error(String mensaje) {
    JOptionPane.showMessageDialog(this, mensaje, "Error",
                                  JOptionPane.ERROR_MESSAGE);
  }

  private String textoCelda(int fila, int columna) {
    Object valor = modelo.getValueAt(fila, columna);

    if (valor == null) {
      return "";
    }

    return valor.toString();
  }
}
