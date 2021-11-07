package net.lihui.app.plugin.thoughtworkscodereviewtools.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrelloConfigDate {
    private String key;
    private String value;

    public TrelloConfigDate(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
