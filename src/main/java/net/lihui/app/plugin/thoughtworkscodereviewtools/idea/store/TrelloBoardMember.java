package net.lihui.app.plugin.thoughtworkscodereviewtools.idea.store;

import com.intellij.util.xmlb.annotations.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TrelloBoardMember {
    @Tag("id")
    private String id;
    @Tag("username")
    private String username;
    @Tag("fullName")
    private String fullName;
}
