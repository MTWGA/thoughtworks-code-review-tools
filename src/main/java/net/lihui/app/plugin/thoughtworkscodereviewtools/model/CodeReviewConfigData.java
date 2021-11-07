package net.lihui.app.plugin.thoughtworkscodereviewtools.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CodeReviewConfigData {
    List<TrelloConfigDate> trelloConfigDateList;

    public List<TrelloConfigDate> getTrelloConfigDateList() {
        return trelloConfigDateList;
    }

    public void setTrelloConfigDateList(List<TrelloConfigDate> trelloConfigDateList) {
        this.trelloConfigDateList = trelloConfigDateList;
    }
}
