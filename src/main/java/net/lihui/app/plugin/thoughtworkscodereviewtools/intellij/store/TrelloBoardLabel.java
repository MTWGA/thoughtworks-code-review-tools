package net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store;

import com.intellij.util.xmlb.annotations.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TrelloBoardLabel {
    @Tag("id")
    private String id;
    @Tag("color")
    private String color;
    @Tag("name")
    private String name;
}
