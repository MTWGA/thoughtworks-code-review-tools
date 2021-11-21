package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class OwnerCheckboxTableModel extends AbstractTableModel {
    JCheckBox[][] checkBoxList = {
            {new JCheckBox("user1", true), new JCheckBox("user1", true), new JCheckBox("user1", true)},
            {new JCheckBox("user1", true), new JCheckBox("user1", true), new JCheckBox("user1", true)},
            {new JCheckBox("user1", true), new JCheckBox("user1", true), new JCheckBox("user1", true)},
    };

    @Override
    public int getRowCount() {
        return checkBoxList.length;
    }

    @Override
    public int getColumnCount() {
        return checkBoxList[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return checkBoxList[rowIndex][columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public String getColumnName(int column) {
        return "";
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        checkBoxList[rowIndex][columnIndex] = (JCheckBox) aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
