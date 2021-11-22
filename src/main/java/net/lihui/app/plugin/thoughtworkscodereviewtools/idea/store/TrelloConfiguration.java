package net.lihui.app.plugin.thoughtworkscodereviewtools.idea.store;

import com.intellij.util.xmlb.annotations.Tag;

public class TrelloConfiguration {
    @Tag("trelloApiKey")
    private String trelloApiKey;
    @Tag("trelloApiToken")
    private String trelloApiToken;
    @Tag("trelloBoardId")
    private String trelloBoardId;

    public TrelloConfiguration() {
    }

    public boolean isInvalid() {
        return trelloApiKey.isBlank() || trelloApiToken.isBlank() || trelloBoardId.isBlank();
    }

    public String getTrelloApiKey() {
        return trelloApiKey;
    }

    public String getTrelloApiToken() {
        return trelloApiToken;
    }

    public String getTrelloBoardId() {
        return trelloBoardId;
    }

    public void setTrelloApiKey(String trelloApiKey) {
        this.trelloApiKey = trelloApiKey;
    }

    public void setTrelloApiToken(String trelloApiToken) {
        this.trelloApiToken = trelloApiToken;
    }

    public void setTrelloBoardId(String trelloBoardId) {
        this.trelloBoardId = trelloBoardId;
    }
}
