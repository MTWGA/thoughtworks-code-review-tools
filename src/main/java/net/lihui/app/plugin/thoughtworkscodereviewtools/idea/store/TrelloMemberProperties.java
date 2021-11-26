package net.lihui.app.plugin.thoughtworkscodereviewtools.idea.store;

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
public class TrelloMemberProperties {
    @XCollection(propertyElementName = "trelloBoardMembers", style = XCollection.Style.v2)
    private List<TrelloBoardMember> trelloBoardMembers = new ArrayList<>();
}
