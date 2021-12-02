// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// lihui change the file start 2021

package net.lihui.app.plugin.thoughtworkscodereviewtools.idea.controller;

import com.intellij.openapi.options.Configurable;
import com.julienvey.trello.domain.Member;
import net.lihui.app.plugin.thoughtworkscodereviewtools.client.TrelloClient;
import net.lihui.app.plugin.thoughtworkscodereviewtools.idea.store.TrelloBoardMemberState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.idea.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.idea.store.TrelloMemberProperties;
import net.lihui.app.plugin.thoughtworkscodereviewtools.idea.store.TrelloState;
import net.lihui.app.plugin.thoughtworkscodereviewtools.service.CodeReviewBoardService;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.TwCodeReviewSettingsComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

import static net.lihui.app.plugin.thoughtworkscodereviewtools.mapper.MemberMapper.MEMBER_MAPPER;

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
        TrelloConfiguration trelloConfiguration = TrelloState.getInstance().getState();

        return !trelloConfiguration.equals(twCodeReviewSettingsComponent.getCurrentTrelloConfiguration());
    }

    @Override
    public void apply() {
        TrelloState trelloState = TrelloState.getInstance();
        trelloState.setTrelloConfiguration(twCodeReviewSettingsComponent.getCurrentTrelloConfiguration());

        fetchBoardMemberList();
    }

    private void fetchBoardMemberList() {
        TrelloConfiguration trelloConfiguration = TrelloState.getInstance().getState();
        TrelloBoardMemberState boardMemberState = TrelloBoardMemberState.getInstance();

        CodeReviewBoardService codeReviewBoardService = new CodeReviewBoardService(new TrelloClient(trelloConfiguration));
        List<Member> trelloBoardMembers = codeReviewBoardService.getTrelloBoardMembers();
        boardMemberState.setTrelloMemberProperties(new TrelloMemberProperties(MEMBER_MAPPER.toStateList(trelloBoardMembers)));
    }

    @Override
    public void reset() {
        TrelloState settings = TrelloState.getInstance();
        TrelloConfiguration trelloConfiguration = settings.getState();

        twCodeReviewSettingsComponent.setTrelloApiKey(trelloConfiguration.getTrelloApiKey());
        twCodeReviewSettingsComponent.setTrelloApiToken(trelloConfiguration.getTrelloApiToken());
        twCodeReviewSettingsComponent.setTrelloBoardId(trelloConfiguration.getTrelloBoardId());
    }

    @Override
    public void disposeUIResources() {
        twCodeReviewSettingsComponent = null;
    }

}
