package com.sqlpractica;

import com.sqlpractica.ui.EmpleadoPanel;
import com.sqlpractica.ui.HomePanel;
import com.sqlpractica.ui.NominaPanel;
import com.sqlpractica.ui.OcupaPanel;
import com.sqlpractica.ui.PlazaPanel;
import com.sqlpractica.ui.TipoPlazaPanel;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** Ventana principal de la aplicación. */
public class AppFrame extends JFrame {

  private static final String HOME = "home";
  private static final String EMPLEADOS = "empleados";
  private static final String PLAZAS = "plazas";
  private static final String TIPOS_PLAZA = "tipos_plaza";
  private static final String OCUPACIONES = "ocupaciones";
  private static final String NOMINAS = "nominas";

  private final CardLayout navigation = new CardLayout();
  private final JPanel screens = new JPanel(navigation);

  public AppFrame() {
    super("Programación con base de datos");

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(900, 650);
    setLocationRelativeTo(null);

    screens.add(new HomePanel(this::showEmpleados, this::showPlazas,
                              this::showTiposPlaza, this::showOcupaciones,
                              this::showNominas),
                HOME);
    screens.add(new EmpleadoPanel(this::showHome), EMPLEADOS);
    screens.add(new PlazaPanel(this::showHome), PLAZAS);
    screens.add(new TipoPlazaPanel(this::showHome), TIPOS_PLAZA);
    screens.add(new OcupaPanel(this::showHome), OCUPACIONES);
    screens.add(new NominaPanel(this::showHome), NOMINAS);

    add(screens);
    showHome();
  }

  private void showHome() { show(HOME); }

  private void showEmpleados() { show(EMPLEADOS); }

  private void showPlazas() { show(PLAZAS); }

  private void showTiposPlaza() { show(TIPOS_PLAZA); }

  private void showOcupaciones() { show(OCUPACIONES); }

  private void showNominas() { show(NOMINAS); }

  private void show(String screen) { navigation.show(screens, screen); }
}
