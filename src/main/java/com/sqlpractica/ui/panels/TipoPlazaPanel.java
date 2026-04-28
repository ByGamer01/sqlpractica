package com.sqlpractica.ui.panels;

import com.sqlpractica.dao.TipoPlazaDAO;
import com.sqlpractica.exception.DAOException;
import com.sqlpractica.model.TipoPlaza;
import com.sqlpractica.ui.components.StyledTextField;

import javax.swing.Box;
import java.awt.Component;
import java.util.List;

public class TipoPlazaPanel extends BaseCrudPanel {

    private final TipoPlazaDAO dao = new TipoPlazaDAO();

    private final StyledTextField tfNombre  = new StyledTextField();
    private final StyledTextField tfFuncion = new StyledTextField();

    public TipoPlazaPanel() {
        super(new String[]{"Nombre", "Función"});
        buildForm();
        reloadTable();
    }

    @Override
    protected void buildForm() {
        formPanel.add(StyledTextField.makeLabel("Nombre (clave primaria)"));
        tfNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(tfNombre);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(StyledTextField.makeLabel("Función"));
        tfFuncion.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(tfFuncion);

        addActionButtons();
    }

    @Override
    public void reloadTable() {
        try {
            tableModel.setRowCount(0);
            List<TipoPlaza> lista = dao.findAll();
            for (TipoPlaza t : lista) {
                tableModel.addRow(new Object[]{t.getNombre(), t.getFuncion()});
            }
        } catch (DAOException ex) {
            showError(ex.getMessage());
        }
    }

    @Override
    protected void onRowSelected(int rowIndex) {
        tfNombre.setText(str(tableModel.getValueAt(rowIndex, 0)));
        tfFuncion.setText(str(tableModel.getValueAt(rowIndex, 1)));
    }

    private static String str(Object o) { return o == null ? "" : o.toString(); }
}
