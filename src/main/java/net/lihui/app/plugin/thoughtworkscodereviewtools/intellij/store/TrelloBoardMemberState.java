package net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@State(
        name = "net.lihui.app.plugin.thoughtworkscodereviewtools.settings.TrelloBoardMemberState",
        storages = @Storage("$APP_CONFIG$/TwCodeReviewToolsSetting.xml")
)
public class TrelloBoardMemberState implements PersistentStateComponent<TrelloMemberProperties> {
    private final TrelloMemberProperties trelloMemberProperties = new TrelloMemberProperties();

    public static TrelloBoardMemberState getInstance() {
        return ApplicationManager.getApplication().getService(TrelloBoardMemberState.class);
    }

    public void updateTrelloBoardMemberList(List<TrelloBoardMember> trelloBoardMemberList) {
        this.trelloMemberProperties.setTrelloBoardMembers(trelloBoardMemberList);
    }

    @Override
    public @Nullable TrelloMemberProperties getState() {
        return trelloMemberProperties;
    }

    @Override
    public void loadState(@NotNull TrelloMemberProperties state) {
        XmlSerializerUtil.copyBean(state, trelloMemberProperties);
    }
}
