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
        name = "net.lihui.app.plugin.thoughtworkscodereviewtools.settings.TrelloBoardLabelState",
        storages = @Storage("$APP_CONFIG$/TwCodeReviewToolsSetting.xml")
)
public class TrelloBoardLabelState implements PersistentStateComponent<TrelloLabelProperties> {
    private TrelloLabelProperties trelloLabelProperties = new TrelloLabelProperties();

    public static TrelloBoardLabelState getInstance() {
        return ApplicationManager.getApplication().getService(TrelloBoardLabelState.class);
    }

    public void updateTrelloBoardLabelList(List<TrelloBoardLabel> trelloLabelProperties) {
        this.trelloLabelProperties.setTrelloBoardLabels(trelloLabelProperties);
    }

    @Override
    public @Nullable TrelloLabelProperties getState() {
        return trelloLabelProperties;
    }

    @Override
    public void loadState(@NotNull TrelloLabelProperties state) {
        XmlSerializerUtil.copyBean(state, trelloLabelProperties);
    }
}
