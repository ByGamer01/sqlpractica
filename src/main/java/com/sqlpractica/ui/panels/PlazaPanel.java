package com.sqlpractica.ui.panels;

import com.sqlpractica.dao.PlazaDAO;
import com.sqlpractica.exception.DAOException;
import com.sqlpractica.model.Plaza;
import com.sqlpractica.ui.components.StyledTextField;

import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;

public class PlazaPanel extends BaseCrudPanel {

    private final PlazaDAO dao = new PlazaDAO();

    private final StyledTextField tfCodigo        = new StyledTextField();
    private final StyledTextField tfNombre        = new StyledTextField();
    private final StyledTextField tfSalario       = new StyledTextField();
    private final StyledTextField tfSupervisora   = new StyledTextField();
    private final StyledTextField tfInforme       = new StyledTextField();
    private final StyledTextField tfTipo          = new StyledTextField();

    public PlazaPanel() {
        super(new String[]{"Código", "Nombre", "Salario", "Supervisora", "Tipo"});
        buildForm();
        reloadTable();
    }

    @Override
    protected void buildForm() {
        JPanel row1 = new JPanel(new GridLayout(1, 2, 8, 0));
        row1.setOpaque(false);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);
        row1.add(col("Código (PK)", tfCodigo));
        row1.add(col("Nombre", tfNombre));
        formPanel.add(row1);
        formPanel.add(Box.createVerticalStrut(8));

        JPanel row2 = new JPanel(new GridLayout(1, 2, 8, 0));
        row2.setOpaque(false);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        row2.add(col("Salario (€)", tfSalario));
        row2.add(col("Nombre tipo plaza", tfTipo));
        formPanel.add(row2);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(StyledTextField.makeLabel("Código plaza supervisora (opcional)"));
        tfSupervisora.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(tfSupervisora);
        formPanel.add(Box.createVerticalStrut(8));

        formPanel.add(StyledTextField.makeLabel("Informe supervisión (opcional)"));
        tfInforme.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(tfInforme);

        addActionButtons();
    }

    private JPanel col(String label, StyledTextField field) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new javax.swing.BoxLayout(p, javax.swing.BoxLayout.Y_AXIS));
        p.add(StyledTextField.makeLabel(label));
        p.add(field);
        return p;
    }

    @Override
    public void reloadTable() {
        try {
            tableModel.setRowCount(0);
            List<Plaza> lista = dao.findAll();
            for (Plaza p : lista) {
                tableModel.addRow(new Object[]{
                        p.getCodigo(),
                        p.getNombre(),
                        String.format("%.2f", p.getSalario()),
                        p.getCodigoPlazaSupervisora() == null ? "" : p.getCodigoPlazaSupervisora(),
                        p.getNombreTipoPlaza()
                });
            }
        } catch (DAOException ex) {
            showError(ex.getMessage());
        }
    }

    @Override
    protected void onRowSelected(int rowIndex) {
        try {
            tfCodigo.setText(str(tableModel.getValueAt(rowIndex, 0)));
            tfNombre.setText(str(tableModel.getValueAt(rowIndex, 1)));
            tfSalario.setText(str(tableModel.getValueAt(rowIndex, 2)).replace(",", "."));
            tfSupervisora.setText(str(tableModel.getValueAt(rowIndex, 3)));
            tfTipo.setText(str(tableModel.getValueAt(rowIndex, 4)));
            for (Plaza p : dao.findAll()) {
                if (p.getCodigo().equals(tfCodigo.getText())) {
                    tfInforme.setText(p.getInformeSupervision() == null ? "" : p.getInformeSupervision());
                    break;
                }
            }
        } catch (DAOException ex) {
            showError(ex.getMessage());
        }
    }

    private static String str(Object o) { return o == null ? "" : o.toString(); }
}
