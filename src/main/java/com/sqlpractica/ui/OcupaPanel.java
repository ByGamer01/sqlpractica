package com.sqlpractica.ui;

import com.sqlpractica.dao.OcupaDAO;
import com.sqlpractica.model.Ocupa;
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

/**
 * Panel CRUD para la tabla 'ocupa'
 *
 * NOVEDAD: la PK es COMPUESTA (NSS + código plaza). Por eso, para
 * editar o eliminar, necesitamos las DOS claves identificando la fila
 *
 * Las fechas se introducen como texto en formato yyyy-MM-dd
 * Para validar que el usuario escribe una fecha real, usamos
 * LocalDate.parse(): si el formato es incorrecto lanza una
 * DateTimeParseException, que capturamos y convertimos en un mensaje
 * claro al usuario
 *
 * Doc LocalDate.parse:
 *   https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html#parse(java.lang.CharSequence)
 */
public class OcupaPanel extends JPanel {

  private final OcupaDAO dao = new OcupaDAO();

  private final DefaultTableModel modelo =
      new DefaultTableModel(new String[] {"NSS empleado", "Código plaza",
                                          "Fecha inicio", "Fecha fin"},
                            0) {
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
    List<Ocupa> lista = dao.obtenerTodos();
    if (lista == null) {
      error(dao.getMensajeError());
      return;
    }
    modelo.setRowCount(0);
    for (Ocupa o : lista) {
      String fin = o.getFechaFin();
      modelo.addRow(new Object[] {o.getNssEmpleado(), o.getCodigoPlaza(),
                                  o.getFechaInicio(), fin == null ? "" : fin});
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
   * Construimos un Ocupa a partir del formulario
   * Validamos los campos obligatorios y el formato de las fechas
   * Mostramos un error y devolvemos null si la validación falla
   *
   * Las fechas se validan con LocalDate.parse(): si el formato es
   * incorrecto (no es yyyy-MM-dd) lanza DateTimeParseException,
   * que capturamos para mostrar un mensaje claro al usuario
   */
  private Ocupa leerFormulario() {
    if (tfNss.getText().isBlank()) {
      error("Falta el NSS del empleado.");
      return null;
    }
    if (tfPlaza.getText().isBlank()) {
      error("Falta el código de la plaza.");
      return null;
    }
    if (tfFechaInicio.getText().isBlank()) {
      error("Falta la fecha de inicio.");
      return null;
    }

    // Validar formato fecha inicio
    try {
      LocalDate.parse(tfFechaInicio.getText().trim());

    } catch (DateTimeParseException ex) {
      error("Fecha incorrecta: " + tfFechaInicio.getText().trim() +
            " (usa yyyy-MM-dd)");
      return null;
    }

    // Validar formato fecha fin solo si se ha rellenado
    if (!tfFechaFin.getText().isBlank()) {
      try {
        LocalDate.parse(tfFechaFin.getText().trim());

      } catch (DateTimeParseException ex) {
        error("Fecha incorrecta: " + tfFechaFin.getText().trim() +
              " (usa yyyy-MM-dd)");
        return null;
      }
    }

    String fin = tfFechaFin.getText().trim();

    return new Ocupa(tfNss.getText().trim(), tfPlaza.getText().trim(),
                     tfFechaInicio.getText().trim(),
                     fin.isEmpty() ? null : fin);
  }

  private void crear() {
    Ocupa o = leerFormulario();
    if (o == null) {
      return;
    }
    // Suele ser FK fallida: NSS o codigo_plaza no existen
    if (!dao.insertar(o)) {
      error(dao.getMensajeError());
      return;
    }
    recargar();
    limpiar();
  }

  /**
   * Para editar hacen falta las DOS partes de la PK (NSS + plaza),
   * por eso comprobamos las dos antes
   */
  private void editar() {
    if (tfNss.getText().isBlank() || tfPlaza.getText().isBlank()) {
      error("Selecciona una ocupación.");
      return;
    }

    Ocupa o = leerFormulario();

    if (o == null) {
      return;
    }
    if (!dao.actualizar(o)) {
      error(dao.getMensajeError());
      return;
    }

    recargar();
  }

  private void eliminar() {
    if (tfNss.getText().isBlank() || tfPlaza.getText().isBlank()) {
      error("Selecciona una ocupación.");
      return;
    }

    int respuesta =
        JOptionPane.showConfirmDialog(this, "¿Eliminar esta ocupación?",
                                      "Confirmar", JOptionPane.YES_NO_OPTION);

    if (respuesta != JOptionPane.YES_OPTION) {
      return;
    }
    if (!dao.eliminar(tfNss.getText().trim(), tfPlaza.getText().trim())) {
      error(dao.getMensajeError());
      return;
    }

    recargar();
    limpiar();
  }

  private void limpiar() {
    tfNss.setText("");
    tfPlaza.setText("");
    tfFechaInicio.setText("");
    tfFechaFin.setText("");
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
