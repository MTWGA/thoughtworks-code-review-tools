// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// lihui change the file start 2021

package net.lihui.app.plugin.thoughtworkscodereviewtools.idea.controller;

import com.intellij.openapi.options.Configurable;
import net.lihui.app.plugin.thoughtworkscodereviewtools.idea.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.idea.store.TrelloState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.idea.ui.TwCodeReviewSettingsComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 */
public class TwCodeReviewSettingsConfigurable implements Configurable {

    private TwCodeReviewSettingsComponent twCodeReviewSettingsComponent;

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Tw Code Review Tools";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return twCodeReviewSettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        twCodeReviewSettingsComponent = new TwCodeReviewSettingsComponent();
        return twCodeReviewSettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        TrelloState settings = TrelloState.getInstance();
        TrelloConfiguration trelloConfiguration = settings.getTrelloConfiguration();

        return twCodeReviewSettingsComponent.isConfigurationChanged(trelloConfiguration);
    }

    @Override
    public void apply() {
        TrelloState settings = TrelloState.getInstance();
        TrelloConfiguration trelloConfiguration = settings.getTrelloConfiguration();

        trelloConfiguration.setTrelloApiKey( twCodeReviewSettingsComponent.getTrelloApiKey());
        trelloConfiguration.setTrelloApiToken(twCodeReviewSettingsComponent.getTrelloApiToken());
        trelloConfiguration.setTrelloBoardId(twCodeReviewSettingsComponent.getTrelloBoardId());

        // TODO call service to get user list
    }

    @Override
    public void reset() {
        TrelloState settings = TrelloState.getInstance();
        TrelloConfiguration trelloConfiguration = settings.getTrelloConfiguration();

        twCodeReviewSettingsComponent.setTrelloApiKey(trelloConfiguration.getTrelloApiKey());
        twCodeReviewSettingsComponent.setTrelloApiToken(trelloConfiguration.getTrelloApiToken());
        twCodeReviewSettingsComponent.setTrelloBoardId(trelloConfiguration.getTrelloBoardId());
    }

    @Override
    public void disposeUIResources() {
        twCodeReviewSettingsComponent = null;
    }

}
