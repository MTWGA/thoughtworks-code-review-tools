package net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store;

import com.intellij.util.xmlb.annotations.XCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrelloLabelProperties {
    @XCollection(propertyElementName = "trelloBoardLabels", style = XCollection.Style.v2)
    private List<TrelloBoardLabel> trelloBoardLabels = new ArrayList<>();
}
