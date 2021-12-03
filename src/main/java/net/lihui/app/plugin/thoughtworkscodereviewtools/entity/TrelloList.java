package net.lihui.app.plugin.thoughtworkscodereviewtools.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrelloList {
    private String id;
    private String name;

    public TrelloList(String id) {
        this.id = id;
    }
}
