package com.sqlpractica.ui;

import com.sqlpractica.dao.EmpleadoDAO;
import com.sqlpractica.model.Empleado;
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
 * Panel CRUD para empleados
 *
 * ESTRUCTURA VISUAL (BorderLayout):
 *   - CENTER: tabla con la lista de empleados (dentro de un JScrollPane)
 *   - SOUTH:  formulario con etiquetas y campos de texto + botones
 *
 * LÓGICA / FLUJO:
 *   1. El constructor monta la UI y llama a recargar() para llenar la tabla
 *   2. recargar() pide al DAO la lista de empleados y la mete en el modelo
 *      de la tabla (DefaultTableModel)
 *   3. Cuando el usuario clica una fila, cargarSeleccion() copia los datos
 *      de esa fila a los JTextField del formulario
 *   4. Los botones llaman a crear / editar / eliminar / limpiar:
 *        - crear():    valida el formulario, lo convierte en Empleado y
 *                      llama a dao.insertar(...)
 *        - editar():   ídem pero con dao.actualizar(...)
 *        - eliminar(): pide confirmación al usuario y llama a
 * dao.eliminar(...)
 *        - limpiar():  vacía los campos y deselecciona la tabla
 *   5. Si cualquier llamada al DAO falla, muestra un diálogo de error
 *
 * Doc JTable + DefaultTableModel:
 *   https://docs.oracle.com/javase/tutorial/uiswing/components/table.html
 */
public class EmpleadoPanel extends JPanel {

  // El DAO que hablará con la base de datos
  private final EmpleadoDAO dao = new EmpleadoDAO();

  // Modelo de la tabla: define las columnas y guarda las filas
  // isCellEditable = false -> el usuario NO puede editar la tabla
  // directamente; solo a través del formulario
  private final DefaultTableModel modelo = new DefaultTableModel(
      new String[] {"NSS", "Nombre", "Apellidos", "Email", "IBAN"}, 0) {
    @Override
    public boolean isCellEditable(int fila, int columna) {
      return false;
    }
  };

  // El JTable es la "vista" del modelo
  private final JTable tabla = new JTable(modelo);

  // Un JTextField por cada campo del formulario
  private final JTextField tfNss = new JTextField();
  private final JTextField tfNombre = new JTextField();
  private final JTextField tfApellidos = new JTextField();
  private final JTextField tfEmail = new JTextField();
  private final JTextField tfIban = new JTextField();

  private final Runnable volver;

  public EmpleadoPanel() { this(null); }

  public EmpleadoPanel(Runnable volver) {
    this.volver = volver;
    // BorderLayout permite poner cosas arriba/abajo/centro/izq/dcha
    setLayout(new BorderLayout(8, 8));
    setBackground(UiStyles.BACKGROUND);

    if (volver != null) {
      add(new SectionHeader("Empleados", volver), BorderLayout.NORTH);
    }

    // Solo se puede seleccionar una fila a la vez
    tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Listener: cuando cambie la selección, copiar la fila al formulario
    // El "if (!getValueIsAdjusting())" evita que se dispare 2 veces
    // (una al deseleccionar la anterior, otra al seleccionar la nueva)
    tabla.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        cargarSeleccion();
      }
    });

    // El JScrollPane permite hacer scroll si hay muchas filas
    UiStyles.styleTable(tabla);

    JPanel contenido = UiStyles.contentPanel();
    contenido.add(UiStyles.tableScroll(tabla), BorderLayout.CENTER);
    add(contenido, BorderLayout.CENTER);

    // Formulario con 6 filas y 2 columnas (etiqueta | campo)
    JPanel formulario = UiStyles.formCard(6);
    formulario.add(new JLabel("NSS:"));
    formulario.add(tfNss);
    formulario.add(new JLabel("Nombre:"));
    formulario.add(tfNombre);
    formulario.add(new JLabel("Apellidos:"));
    formulario.add(tfApellidos);
    formulario.add(new JLabel("Email:"));
    formulario.add(tfEmail);
    formulario.add(new JLabel("IBAN:"));
    formulario.add(tfIban);

    // Botones de acción
    JButton btCrear = UiStyles.actionButton("Crear", UiStyles.SUCCESS);
    JButton btEditar = UiStyles.actionButton("Editar", UiStyles.PRIMARY);
    JButton btEliminar = UiStyles.actionButton("Eliminar", UiStyles.DANGER);
    JButton btLimpiar = UiStyles.actionButton("Limpiar", UiStyles.SECONDARY);

    // addActionListener -> qué hacer cuando se pulsa el botón
    // La sintaxis "e -> crear()" es una expresión lambda: una forma
    // corta de escribir un método anónimo
    UiStyles.styleTextField(tfNss);
    UiStyles.styleTextField(tfNombre);
    UiStyles.styleTextField(tfApellidos);
    UiStyles.styleTextField(tfEmail);
    UiStyles.styleTextField(tfIban);

    btCrear.addActionListener(e -> crear());
    btEditar.addActionListener(e -> editar());
    btEliminar.addActionListener(e -> eliminar());
    btLimpiar.addActionListener(e -> limpiar());

    // Los 4 botones en una fila (la última del formulario)
    JPanel botones = UiStyles.buttonRow();
    botones.add(btCrear);
    botones.add(btEditar);
    botones.add(btEliminar);
    botones.add(btLimpiar);
    formulario.add(botones);

    contenido.add(formulario, BorderLayout.SOUTH);

    // Carga inicial de la tabla
    recargar();
  }

  /**
   * Vacia el modelo y vuelve a leer todos los empleados de la BD
   * setRowCount(0) -> elimina todas las filas existentes
   */
  private void recargar() {
    List<Empleado> lista = dao.obtenerTodos();
    if (lista == null) {
      error(dao.getMensajeError());
      return;
    }
    modelo.setRowCount(0);
    for (Empleado e : lista) {
      // addRow añade una fila al final de la tabla
      modelo.addRow(new Object[] {e.getNss(), e.getNombre(), e.getApellidos(),
                                  e.getEmail(), e.getIban()});
    }
  }

  /**
   * Copia los datos de la fila seleccionada a los campos del formulario,
   * para poder editar o eliminar ese empleado
   */
  private void cargarSeleccion() {
    int fila = tabla.getSelectedRow();
    if (fila < 0) {
      // No hay nada seleccionado
      return;
    }
    tfNss.setText(textoCelda(fila, 0));
    tfNombre.setText(textoCelda(fila, 1));
    tfApellidos.setText(textoCelda(fila, 2));
    tfEmail.setText(textoCelda(fila, 3));
    tfIban.setText(textoCelda(fila, 4));
  }

  /**
   * Crea un empleado nuevo a partir de los campos del formulario
   * Validamos primero que los campos obligatorios no estén en blanco
   */
  private void crear() {
    if (tfNss.getText().isBlank() || tfNombre.getText().isBlank() ||
        tfApellidos.getText().isBlank()) {
      error("NSS, nombre y apellidos son obligatorios.");
      return;
    }
    Empleado nuevo =
        new Empleado(tfNss.getText().trim(), tfNombre.getText().trim(),
                     tfApellidos.getText().trim(), tfEmail.getText().trim(),
                     tfIban.getText().trim());
    if (!dao.insertar(nuevo)) {
      error(dao.getMensajeError());
      return;
    }
    recargar();
    limpiar();
  }

  /**
   * Actualiza el empleado seleccionado
   * El NSS no se cambia (es la PK): se usa para localizar la fila
   */
  private void editar() {
    if (tfNss.getText().isBlank()) {
      error("Selecciona un empleado.");
      return;
    }
    Empleado modificado =
        new Empleado(tfNss.getText().trim(), tfNombre.getText().trim(),
                     tfApellidos.getText().trim(), tfEmail.getText().trim(),
                     tfIban.getText().trim());
    if (!dao.actualizar(modificado)) {
      error(dao.getMensajeError());
      return;
    }
    recargar();
  }

  /**
   * Borra el empleado seleccionado, pidiendo confirmación primero
   */
  private void eliminar() {
    if (tfNss.getText().isBlank()) {
      error("Selecciona un empleado.");
      return;
    }
    int respuesta = JOptionPane.showConfirmDialog(
        this, "¿Eliminar al empleado " + tfNss.getText() + "?", "Confirmar",
        JOptionPane.YES_NO_OPTION);
    if (respuesta != JOptionPane.YES_OPTION) {
      // El usuario ha pulsado "No" o ha cerrado el diálogo
      return;
    }
    if (!dao.eliminar(tfNss.getText().trim())) {
      error(dao.getMensajeError());
      return;
    }
    recargar();
    limpiar();
  }

  /** Vacía los campos y deselecciona la tabla */
  private void limpiar() {
    tfNss.setText("");
    tfNombre.setText("");
    tfApellidos.setText("");
    tfEmail.setText("");
    tfIban.setText("");
    tabla.clearSelection();
  }

  /** Muestra un diálogo de error con el mensaje indicado */
  private void error(String mensaje) {
    JOptionPane.showMessageDialog(this, mensaje, "Error",
                                  JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Devuelve el contenido de una celda como String
   * Si la celda es null (campo opcional vacío), devuelve "" en vez
   * de "null", para no liar al usuario
   */
  private String textoCelda(int fila, int columna) {
    Object valor = modelo.getValueAt(fila, columna);
    if (valor == null) {
      return "";
    }
    return valor.toString();
  }
}
