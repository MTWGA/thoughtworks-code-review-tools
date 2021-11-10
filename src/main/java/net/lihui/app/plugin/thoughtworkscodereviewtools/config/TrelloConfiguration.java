package net.lihui.app.plugin.thoughtworkscodereviewtools.config;

public class TrelloConfiguration {
    private String trelloApiKey;
    private String trelloApiToken;
    private String trelloBoardId;

    public TrelloConfiguration(String trelloApiKey, String trelloApiToken, String trelloBoardId) {
        this.trelloApiKey = trelloApiKey;
        this.trelloApiToken = trelloApiToken;
        this.trelloBoardId = trelloBoardId;
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
}
