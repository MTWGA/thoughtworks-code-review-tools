package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerCheckboxDTO;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TableCheckboxCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        OwnerCheckboxDTO ownerCheckboxDTO = (OwnerCheckboxDTO) value;
        return new JCheckBox(ownerCheckboxDTO.getFullName(), ownerCheckboxDTO.isSelected());
    }
}
