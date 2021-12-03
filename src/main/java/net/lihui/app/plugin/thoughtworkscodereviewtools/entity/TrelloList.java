package net.lihui.app.plugin.thoughtworkscodereviewtools.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrelloList {
    private String id;
    private String name;

    public TrelloList(String id) {
        this.id = id;
    }
}
