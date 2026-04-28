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

    @Override
    protected void onCreate() {
        if (tfNombre.getText().isBlank()) throw new RuntimeException("El campo Nombre es obligatorio.");
        TipoPlaza t = new TipoPlaza(tfNombre.getText().trim(), tfFuncion.getText().trim());
        wrap(() -> dao.insert(t));
        reloadTable();
        onClear();
        showInfo("Tipo de plaza creado.");
    }

    @Override
    protected void onUpdate() {
        if (tfNombre.getText().isBlank()) throw new RuntimeException("Selecciona un tipo de plaza.");
        TipoPlaza t = new TipoPlaza(tfNombre.getText().trim(), tfFuncion.getText().trim());
        wrap(() -> dao.update(t));
        reloadTable();
        showInfo("Tipo actualizado.");
    }

    @Override
    protected void onDelete() {
        if (tfNombre.getText().isBlank()) throw new RuntimeException("Selecciona un tipo para eliminar.");
        if (!confirm("¿Eliminar el tipo '" + tfNombre.getText() + "'?")) return;
        String nombre = tfNombre.getText().trim();
        wrap(() -> dao.delete(nombre));
        reloadTable();
        onClear();
        showInfo("Tipo eliminado.");
    }

    @Override
    protected void onClear() {
        tfNombre.setText("");
        tfFuncion.setText("");
        table.clearSelection();
    }

    private static String str(Object o) { return o == null ? "" : o.toString(); }
}
