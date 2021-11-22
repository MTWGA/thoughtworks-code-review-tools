// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// lihui change the file start 2021

package net.lihui.app.plugin.thoughtworkscodereviewtools.idea.store;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "net.lihui.app.plugin.thoughtworkscodereviewtools.settings.TrelloState",
        storages = @Storage("$APP_CONFIG$/TwCodeReviewToolsSetting.xml")
)
public class TrelloState implements PersistentStateComponent<TrelloConfiguration> {

    private TrelloConfiguration trelloConfiguration = new TrelloConfiguration();

    public static TrelloState getInstance() {
        return ApplicationManager.getApplication().getService(TrelloState.class);
    }

    @Nullable
    @Override
    public TrelloConfiguration getState() { // idea 保存时调用，拿到数据，保存到文件中
        return trelloConfiguration;
    }

    @Override
    public void loadState(@NotNull TrelloConfiguration state) {
        XmlSerializerUtil.copyBean(state, trelloConfiguration);
    }

    public TrelloConfiguration getTrelloConfiguration() {
        return trelloConfiguration;
    }

}
