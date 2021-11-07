// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// lihui change the file start 2021

package net.lihui.app.plugin.thoughtworkscodereviewtools.store;

import com.intellij.openapi.options.Configurable;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.TwCodeReviewSettingsComponent;
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
        TwCodeReviewSettingsState settings = TwCodeReviewSettingsState.getInstance();
        return !twCodeReviewSettingsComponent.getTrelloApiKey().equals(settings.trelloApiKey)
                && !twCodeReviewSettingsComponent.getTrelloApiToken().equals(settings.trelloApiToken)
                && !twCodeReviewSettingsComponent.getTrelloBoardId().equals(settings.trelloBoardId);
    }

    @Override
    public void apply() {
        TwCodeReviewSettingsState settings = TwCodeReviewSettingsState.getInstance();
        settings.trelloApiKey = twCodeReviewSettingsComponent.getTrelloApiKey();
        settings.trelloApiToken = twCodeReviewSettingsComponent.getTrelloApiToken();
        settings.trelloBoardId = twCodeReviewSettingsComponent.getTrelloBoardId();

    }

    @Override
    public void reset() {
        TwCodeReviewSettingsState settings = TwCodeReviewSettingsState.getInstance();
        twCodeReviewSettingsComponent.setTrelloApiKey(settings.trelloApiKey);
        twCodeReviewSettingsComponent.setTrelloApiToken(settings.trelloApiToken);
        twCodeReviewSettingsComponent.setTrelloBoardId(settings.trelloBoardId);
    }

    @Override
    public void disposeUIResources() {
        twCodeReviewSettingsComponent = null;
    }

}
