package net.lihui.app.plugin.thoughtworkscodereviewtools.store;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.XmlSerializerUtil;
import net.lihui.app.plugin.thoughtworkscodereviewtools.model.CodeReviewConfigData;
import net.lihui.app.plugin.thoughtworkscodereviewtools.model.TrelloConfigDate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

@State(name = "TWGitCommitMessageHelperSettings", storages = {@Storage("$APP_CONFIG$/TwCodeReviewToolsStore-settings.xml")})
public class TwCodeReviewToolsStore implements PersistentStateComponent<TwCodeReviewToolsStore> {
    private static final Logger log = Logger.getInstance(TwCodeReviewToolsStore.class);

    private CodeReviewConfigData codeReviewConfigData;

    public TwCodeReviewToolsStore(CodeReviewConfigData codeReviewConfigData) {
        this.codeReviewConfigData = codeReviewConfigData;
    }

    @Override
    public @Nullable TwCodeReviewToolsStore getState() {
        if (this.codeReviewConfigData == null) {
            return null;
        }
        return this;
    }

    @Override
    public void loadState(@NotNull TwCodeReviewToolsStore state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    private void initDefaultStore() {
        codeReviewConfigData = new CodeReviewConfigData();
        try {
            List<TrelloConfigDate> trelloConfigDateList = new LinkedList<>();
            trelloConfigDateList.add(new TrelloConfigDate("trelloKey", "5539a8fe5e55167267f18ea549372f0c"));
            trelloConfigDateList.add(new TrelloConfigDate("trelloAccessToken", "please fill you toke to here"));
            trelloConfigDateList.add(new TrelloConfigDate("trelloCodeReviewBoardId", "such as OpTkznTN"));
            codeReviewConfigData.setTrelloConfigDateList(trelloConfigDateList);
        } catch (Exception e) {
            log.error("init data fail", e);
        }
    }

    public CodeReviewConfigData getCodeReviewConfigData() {
        if (codeReviewConfigData == null) {
            initDefaultStore();
        }
        return codeReviewConfigData;
    }

    private void setCodeReviewConfigData(CodeReviewConfigData codeReviewConfigData) {
        this.codeReviewConfigData = codeReviewConfigData;
    }

}
