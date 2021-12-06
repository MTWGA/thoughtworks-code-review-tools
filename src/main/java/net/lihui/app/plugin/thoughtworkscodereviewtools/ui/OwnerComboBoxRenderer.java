package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerCheckboxDTO;

import javax.swing.*;
import java.awt.*;

public class OwnerComboBoxRenderer extends JLabel implements ListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList jList, Object o, int i, boolean b, boolean b1) {
        OwnerCheckboxDTO ownerCheckboxDTO = (OwnerCheckboxDTO) o;
        setText(ownerCheckboxDTO.getFullName());
        return this;
    }
}
