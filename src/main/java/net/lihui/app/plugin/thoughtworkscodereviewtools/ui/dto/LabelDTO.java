package net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LabelDTO {
    private String id;
    private String idBoard;
    private String color;
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
