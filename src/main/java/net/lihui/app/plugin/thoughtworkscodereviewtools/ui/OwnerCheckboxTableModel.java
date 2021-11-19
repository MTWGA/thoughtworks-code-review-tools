package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class OwnerCheckboxTableModel extends AbstractTableModel {


    @Override
    public int getRowCount() {
        return 50;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return new JCheckBox("user1", true);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return JCheckBox.class;
    }

    @Override
    public String getColumnName(int column) {
        return "";
    }
}
