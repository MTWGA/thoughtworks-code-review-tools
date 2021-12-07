package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerDTO;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

public class CustomizedObjectToStringConverter extends ObjectToStringConverter {
    @Override
    public String getPreferredStringForItem(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return o.toString();
        }
        return ((OwnerDTO) o).getFullName();
    }
}
