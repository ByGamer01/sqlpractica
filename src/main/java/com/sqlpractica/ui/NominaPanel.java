package com.sqlpractica.ui;

import com.sqlpractica.dao.NominaDAO;
import com.sqlpractica.model.Nomina;
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
 * Panel CRUD para la tabla 'nomina'
 *
 * NOVEDAD: el ID es AUTOINCREMENT (lo asigna SQLite). Por eso:
 *   - El JTextField del ID está marcado como NO editable
 *   - Al CREAR: pasamos id = null al objeto Nomina; tras el insert
 *     el DAO le asigna el id real generado por la BD
 *   - Al EDITAR: necesitamos que haya un id seleccionado; el método
 *     leerFormulario(true) lo lee y lo convierte a Integer
 */
public class NominaPanel extends JPanel {

  private final NominaDAO dao = new NominaDAO();

  private final DefaultTableModel modelo =
      new DefaultTableModel(new String[] {"ID", "IBAN pago", "Importe",
                                          "NSS empleado", "Código plaza"},
                            0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
          return false;
        }
      };

  private final JTable tabla = new JTable(modelo);

  private final JTextField tfId = new JTextField();
  private final JTextField tfIban = new JTextField();
  private final JTextField tfImporte = new JTextField();
  private final JTextField tfNss = new JTextField();
  private final JTextField tfPlaza = new JTextField();

  public NominaPanel() {
    setLayout(new BorderLayout(8, 8));

    // El ID lo genera la BD: el usuario no debería tocarlo a mano
    tfId.setEditable(false);

    tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tabla.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        cargarSeleccion();
      }
    });
    add(new JScrollPane(tabla), BorderLayout.CENTER);

    JPanel formulario = new JPanel(new GridLayout(6, 2, 4, 4));
    formulario.add(new JLabel("ID (auto):"));
    formulario.add(tfId);
    formulario.add(new JLabel("IBAN pago:"));
    formulario.add(tfIban);
    formulario.add(new JLabel("Importe:"));
    formulario.add(tfImporte);
    formulario.add(new JLabel("NSS empleado:"));
    formulario.add(tfNss);
    formulario.add(new JLabel("Código plaza:"));
    formulario.add(tfPlaza);

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
    List<Nomina> lista = dao.obtenerTodos();

    if (lista == null) {
      error(dao.getMensajeError());
      return;
    }

    modelo.setRowCount(0);

    for (Nomina n : lista) {
      modelo.addRow(new Object[] {n.getId(), n.getIbanPago(),
                                  n.getImportePago(), n.getNssEmpleado(),
                                  n.getCodigoPlaza()});
    }
  }

  private void cargarSeleccion() {
    int fila = tabla.getSelectedRow();

    if (fila < 0) {
      return;
    }

    tfId.setText(textoCelda(fila, 0));
    tfIban.setText(textoCelda(fila, 1));
    tfImporte.setText(textoCelda(fila, 2));
    tfNss.setText(textoCelda(fila, 3));
    tfPlaza.setText(textoCelda(fila, 4));
  }

  /**
   * Construimos un objeto Nomina a partir del formulario
   * Mostramos un error y devolvemos null si la validación falla
   *
   * @param conId true si queremos leer también el ID (al editar)
   *              false al crear (el ID lo asigna la BD)
   */
  private Nomina leerFormulario(boolean conId) {
    if (tfIban.getText().isBlank()) {
      error("El IBAN es obligatorio.");
      return null;
    }
    if (tfNss.getText().isBlank()) {
      error("El NSS es obligatorio.");
      return null;
    }
    if (tfPlaza.getText().isBlank()) {
      error("El código de plaza es obligatorio.");
      return null;
    }

    // Importe -> double, bien con coma o punto decimal
    double importe;

    try {
      importe =
          Double.parseDouble(tfImporte.getText().trim().replace(",", "."));

    } catch (NumberFormatException ex) {
      error("El importe tiene que ser un número.");
      return null;
    }

    // Solo leemos el ID en modo edición
    Integer id = null;

    if (conId) {
      try {
        id = Integer.parseInt(tfId.getText().trim());

      } catch (NumberFormatException ex) {
        error("Selecciona una nómina (ID inválido).");
        return null;
      }
    }

    return new Nomina(id, tfIban.getText().trim(), importe,
                      tfNss.getText().trim(), tfPlaza.getText().trim());
  }

  private void crear() {
    // false = no leer ID (es null para que la BD lo genere)
    Nomina n = leerFormulario(false);

    if (n == null) {
      return;
    }
    if (!dao.insertar(n)) {
      error(dao.getMensajeError());
      return;
    }

    recargar();
    limpiar();
  }

  private void editar() {
    if (tfId.getText().isBlank()) {
      error("Selecciona una nómina.");
      return;
    }

    // true = leer ID (es la PK que identifica la fila)
    Nomina n = leerFormulario(true);

    if (n == null) {
      return;
    }
    if (!dao.actualizar(n)) {
      error(dao.getMensajeError());
      return;
    }

    recargar();
  }

  private void eliminar() {
    if (tfId.getText().isBlank()) {
      error("Selecciona una nómina.");
      return;
    }

    int respuesta = JOptionPane.showConfirmDialog(
        this, "¿Eliminar la nómina " + tfId.getText() + "?", "Confirmar",
        JOptionPane.YES_NO_OPTION);

    if (respuesta != JOptionPane.YES_OPTION) {
      return;
    }

    int id = Integer.parseInt(tfId.getText().trim());

    if (!dao.eliminar(id)) {
      error(dao.getMensajeError());
      return;
    }
    recargar();
    limpiar();
  }

  private void limpiar() {
    tfId.setText("");
    tfIban.setText("");
    tfImporte.setText("");
    tfNss.setText("");
    tfPlaza.setText("");
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
