package com.sqlpractica.ui.panels;

import com.sqlpractica.dao.EmpleadoDAO;
import com.sqlpractica.exception.DAOException;
import com.sqlpractica.model.Empleado;
import com.sqlpractica.ui.components.StyledTextField;

import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;

public class EmpleadoPanel extends BaseCrudPanel {

    private final EmpleadoDAO dao = new EmpleadoDAO();

    private final StyledTextField tfNss       = new StyledTextField();
    private final StyledTextField tfNombre    = new StyledTextField();
    private final StyledTextField tfApellidos = new StyledTextField();
    private final StyledTextField tfEmail     = new StyledTextField();
    private final StyledTextField tfIban      = new StyledTextField();

    public EmpleadoPanel() {
        super(new String[]{"NSS", "Nombre", "Apellidos", "Email", "IBAN"});
        buildForm();
        reloadTable();
    }

    @Override
    protected void buildForm() {
        formPanel.add(StyledTextField.makeLabel("NSS (clave primaria)"));
        tfNss.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(tfNss);
        formPanel.add(Box.createVerticalStrut(8));

        JPanel row1 = new JPanel(new GridLayout(1, 2, 8, 0));
        row1.setOpaque(false);
        JPanel col1a = new JPanel();
        col1a.setOpaque(false);
        col1a.setLayout(new javax.swing.BoxLayout(col1a, javax.swing.BoxLayout.Y_AXIS));
        col1a.add(StyledTextField.makeLabel("Nombre"));
        col1a.add(tfNombre);
        JPanel col1b = new JPanel();
        col1b.setOpaque(false);
        col1b.setLayout(new javax.swing.BoxLayout(col1b, javax.swing.BoxLayout.Y_AXIS));
        col1b.add(StyledTextField.makeLabel("Apellidos"));
        col1b.add(tfApellidos);
        row1.add(col1a);
        row1.add(col1b);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(row1);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(StyledTextField.makeLabel("Email"));
        tfEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(tfEmail);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(StyledTextField.makeLabel("IBAN"));
        tfIban.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(tfIban);

        addActionButtons();
    }
}
