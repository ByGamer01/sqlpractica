package com.sqlpractica.ui;

import com.sqlpractica.dao.PlazaDAO;
import com.sqlpractica.model.Plaza;
import com.sqlpractica.ui.components.SectionHeader;
import com.sqlpractica.ui.components.UiStyles;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Panel CRUD para la tabla 'plaza'
 *
 * NOVEDADES respecto a EmpleadoPanel:
 *   - Hay un campo numérico (salario): hay que parsearlo a double, y
 *     si el usuario escribe algo no numérico avisamos
 *   - Hay 2 campos opcionales (supervisora e informe): si vienen
 *     vacíos los pasamos como null al objeto Plaza
 *   - leerFormulario() centraliza la validación y la conversión de
 *     campos -> objeto Plaza, así crear() y editar() comparten lógica
 */
public class PlazaPanel extends JPanel {

  private final PlazaDAO dao = new PlazaDAO();
  private final DefaultTableModel modelo =
      new DefaultTableModel(new String[] {"Código", "Nombre", "Salario",
                                          "Supervisora", "Informe", "Tipo"},
                            0) {
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

  private final Runnable volver;

  public PlazaPanel() { this(null); }

  public PlazaPanel(Runnable volver) {
    this.volver = volver;
    setLayout(new BorderLayout(8, 8));
    setBackground(UiStyles.BACKGROUND);

    if (volver != null) {
      add(new SectionHeader("Plazas", volver), BorderLayout.NORTH);
    }

    tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tabla.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        cargarSeleccion();
      }
    });

    UiStyles.styleTable(tabla);

    JPanel contenido = UiStyles.contentPanel();
    contenido.add(UiStyles.tableScroll(tabla), BorderLayout.CENTER);
    add(contenido, BorderLayout.CENTER);

    // 6 filas de campos + 1 fila de botones = 7
    JPanel formulario = UiStyles.formCard(7);
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

    JButton btCrear = UiStyles.actionButton("Crear", UiStyles.SUCCESS);
    JButton btEditar = UiStyles.actionButton("Editar", UiStyles.PRIMARY);
    JButton btEliminar = UiStyles.actionButton("Eliminar", UiStyles.DANGER);
    JButton btLimpiar = UiStyles.actionButton("Limpiar", UiStyles.SECONDARY);

    UiStyles.styleTextField(tfCodigo);
    UiStyles.styleTextField(tfNombre);
    UiStyles.styleTextField(tfSalario);
    UiStyles.styleTextField(tfSupervisora);
    UiStyles.styleTextField(tfInforme);
    UiStyles.styleTextField(tfTipo);

    btCrear.addActionListener(e -> crear());
    btEditar.addActionListener(e -> editar());
    btEliminar.addActionListener(e -> eliminar());
    btLimpiar.addActionListener(e -> limpiar());

    JPanel botones = UiStyles.buttonRow();
    botones.add(btCrear);
    botones.add(btEditar);
    botones.add(btEliminar);
    botones.add(btLimpiar);
    formulario.add(botones);

    contenido.add(formulario, BorderLayout.SOUTH);

    recargar();
  }

  private void recargar() {
    List<Plaza> lista = dao.obtenerTodos();

    if (lista == null) {
      error(dao.getMensajeError());
      return;
    }

    modelo.setRowCount(0);

    for (Plaza p : lista) {
      // Si supervisora o informe son null, mostramos "" en la
      // tabla en vez de la palabra "null"
      String supervisora = p.getCodigoPlazaSupervisora();
      String informe = p.getInformeSupervision();

      modelo.addRow(new Object[] {p.getCodigo(), p.getNombre(), p.getSalario(),
                                  supervisora == null ? "" : supervisora,
                                  informe == null ? "" : informe,
                                  p.getNombreTipoPlaza()});
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
   * Lee los campos del formulario, los valida y construye un objeto Plaza
   * Si algún campo obligatorio está vacío o el salario no es un número,
   * muestra un diálogo de error y devuelve null
   */
  private Plaza leerFormulario() {
    if (tfCodigo.getText().isBlank()) {
      error("El código es obligatorio.");
      return null;
    }
    if (tfNombre.getText().isBlank()) {
      error("El nombre es obligatorio.");
      return null;
    }
    if (tfTipo.getText().isBlank()) {
      error("El tipo de plaza es obligatorio.");
      return null;
    }

    // Salario: convertir el texto a double
    // Aceptamos coma o punto decimal -> reemplazamos coma por punto
    double salario;

    try {
      salario =
          Double.parseDouble(tfSalario.getText().trim().replace(",", "."));

    } catch (NumberFormatException ex) {
      error("El salario tiene que ser un número.");
      return null;
    }

    // Campos opcionales: si están vacíos, null para que la BD guarde NULL
    String supervisora = tfSupervisora.getText().trim();
    String informe = tfInforme.getText().trim();

    return new Plaza(tfCodigo.getText().trim(), tfNombre.getText().trim(),
                     salario, supervisora.isEmpty() ? null : supervisora,
                     informe.isEmpty() ? null : informe,
                     tfTipo.getText().trim());
  }

  private void crear() {
    Plaza p = leerFormulario();

    if (p == null) {
      return;
    }
    if (!dao.insertar(p)) {
      error(dao.getMensajeError());
      return;
    }

    recargar();
    limpiar();
  }

  private void editar() {
    if (tfCodigo.getText().isBlank()) {
      error("Selecciona una plaza.");
      return;
    }

    Plaza p = leerFormulario();

    if (p == null) {
      return;
    }
    if (!dao.actualizar(p)) {
      error(dao.getMensajeError());
      return;
    }

    recargar();
  }

  private void eliminar() {
    if (tfCodigo.getText().isBlank()) {
      error("Selecciona una plaza.");
      return;
    }

    int respuesta = JOptionPane.showConfirmDialog(
        this, "¿Eliminar la plaza " + tfCodigo.getText() + "?", "Confirmar",
        JOptionPane.YES_NO_OPTION);

    if (respuesta != JOptionPane.YES_OPTION) {
      return;
    }
    if (!dao.eliminar(tfCodigo.getText().trim())) {
      error(dao.getMensajeError());
      return;
    }

    recargar();
    limpiar();
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
