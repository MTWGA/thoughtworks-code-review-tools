package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerDTO;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

public class OwnerDTOToStringConverter extends ObjectToStringConverter {
    @Override
    public String getPreferredStringForItem(Object o) {
        return o == null ? null : ((OwnerDTO) o).getFullName();
    }
}
