// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// lihui change the file start 2021

package net.lihui.app.plugin.thoughtworkscodereviewtools.store;

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
        name = "net.lihui.app.plugin.thoughtworkscodereviewtools.settings.AppSettingsState",
        storages = @Storage("$APP_CONFIG$/TwCodeReviewToolsSetting.xml")
)
public class TwCodeReviewSettingsState implements PersistentStateComponent<TwCodeReviewSettingsState> {


    public String trelloApiKey = "";
    public String trelloApiToken = "";
    public String trelloBoardId = "";

    public static TwCodeReviewSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(TwCodeReviewSettingsState.class);
    }

    @Nullable
    @Override
    public TwCodeReviewSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull TwCodeReviewSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
