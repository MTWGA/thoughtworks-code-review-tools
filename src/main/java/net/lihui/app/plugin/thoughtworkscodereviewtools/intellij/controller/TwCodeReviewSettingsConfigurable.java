// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// lihui change the file start 2021

package net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.controller;

import com.intellij.openapi.options.Configurable;
import com.julienvey.trello.TrelloBadRequestException;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardLabel;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardLabelState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMember;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMemberState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.service.CodeReviewBoardService;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.settingView.TwCodeReviewSettingsComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * Provides controller functionality for application settings.
 */
public class TwCodeReviewSettingsConfigurable implements Configurable {

    private static final String SETTING_STATUS_LABEL_OK = "OK";
    private static final String SETTING_STATUS_ERROR_INVALID_ID = "invalid id";
    private static final String SETTING_STATUS_ERROR_INVALID_ID_TIPS = "You trello config is invalid, please check them.";
    private static final String CODE_REVIEW_PLUGIN_DISPLAY_NAME = "TW Code Review Tools";
    private TwCodeReviewSettingsComponent twCodeReviewSettingsComponent;

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return CODE_REVIEW_PLUGIN_DISPLAY_NAME;
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
        TrelloConfiguration trelloConfiguration = TrelloState.getInstance().getState();

        return !trelloConfiguration.equals(twCodeReviewSettingsComponent.getCurrentTrelloConfiguration());
    }

    @Override
    public void apply() {
        TrelloState trelloState = TrelloState.getInstance();
        trelloState.setState(twCodeReviewSettingsComponent.getCurrentTrelloConfiguration());

        try {
            updateBoard();
            twCodeReviewSettingsComponent.setTrelloSettingStatusLabel(SETTING_STATUS_LABEL_OK);
        } catch (TrelloBadRequestException trelloBadRequestException) {
            if (trelloBadRequestException.getMessage().equals(SETTING_STATUS_ERROR_INVALID_ID)) {
                twCodeReviewSettingsComponent.setTrelloSettingStatusLabel(SETTING_STATUS_ERROR_INVALID_ID_TIPS);
            }
        }
    }

    private void updateBoard() {
        TrelloConfiguration trelloConfiguration = TrelloState.getInstance().getState();
        CodeReviewBoardService codeReviewBoardService = new CodeReviewBoardService(trelloConfiguration);
        codeReviewBoardService.updateBoardState();
    }

    @Override
    public void reset() {
        TrelloConfiguration trelloConfiguration = TrelloState.getInstance().getState();

        twCodeReviewSettingsComponent.setTrelloApiKey(trelloConfiguration.getTrelloApiKey());
        twCodeReviewSettingsComponent.setTrelloApiToken(trelloConfiguration.getTrelloApiToken());
        twCodeReviewSettingsComponent.setTrelloBoardId(trelloConfiguration.getTrelloBoardId());
    }

    @Override
    public void disposeUIResources() {
        twCodeReviewSettingsComponent = null;
    }

}
