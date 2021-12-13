package net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store;

import com.intellij.util.xmlb.annotations.Tag;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class TrelloConfiguration {
    @Tag("trelloApiKey")
    private String trelloApiKey;
    @Tag("trelloApiToken")
    private String trelloApiToken;
    @Tag("trelloBoardId")
    private String trelloBoardId;
    @Tag("dueIntervalHours")
    private long dueIntervalHours = 24;

    public boolean isAnyBlank() {
        return trelloApiKey.isBlank() || trelloApiToken.isBlank() || trelloBoardId.isBlank();
    }
}
