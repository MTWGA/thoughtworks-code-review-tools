package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerDTO;

import javax.swing.*;
import java.awt.*;

public class OwnerComboboxRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        OwnerDTO ownerCheckboxDTO = (OwnerDTO) value;
        return super.getListCellRendererComponent(list, ownerCheckboxDTO.getFullName(), index, isSelected, cellHasFocus);
    }
}
