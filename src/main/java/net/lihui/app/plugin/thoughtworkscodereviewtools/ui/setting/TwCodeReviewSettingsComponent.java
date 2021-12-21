// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// lihui change the file start 2021

package net.lihui.app.plugin.thoughtworkscodereviewtools.ui.setting;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.julienvey.trello.NotAuthorizedException;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloConfiguration;
import net.lihui.app.plugin.thoughtworkscodereviewtools.service.CodeReviewBoardService;

import javax.swing.*;
import java.awt.*;

import static net.lihui.app.plugin.thoughtworkscodereviewtools.constant.TrelloRequestErrorConstant.AUTHORIZED_FAILED_NOTIFICATION;
import static net.lihui.app.plugin.thoughtworkscodereviewtools.constant.TrelloRequestErrorConstant.BOARD_ID_INVALID_ERROR_MESSAGE;
import static net.lihui.app.plugin.thoughtworkscodereviewtools.constant.TrelloRequestErrorConstant.INVALID_BOARD_ID_NOTIFICATION;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */

public class TwCodeReviewSettingsComponent {

    private final JPanel mainPanel;
    private final JBTextField trelloApiKeyTextField = new JBTextField();
    private final JBTextField trelloApiTokenTextField = new JBTextField();
    private final JBTextField trelloBoardIdTextField = new JBTextField();
    private final JBTextField trelloDueIntervalTimeTextField = new JBTextField("24");
    private final JBLabel trelloSettingStatusLabel = new JBLabel("");
    private final JButton testTrelloConfigurationButton = new JButton("Test Connection");
    private final JBLabel trelloSettingStatusTipsLabel = new JBLabel("Setting status:");
    private final String CONNECTION_SUCCESS_TIPS = "Connection Success!";
    private final String CONNECTION_FAIL_TIPS = "Connection Fail";

    public TwCodeReviewSettingsComponent() {
        testTrelloConfigurationButton.addActionListener(e -> {
            doTrelloTestConnection();
        });
        trelloSettingStatusTipsLabel.setVisible(false);
        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Enter trello key: "), trelloApiKeyTextField, 1, false)
                .addLabeledComponent(new JBLabel("Enter trello token: "), trelloApiTokenTextField, 1, false)
                .addLabeledComponent(new JBLabel("Enter trello code review board: "), trelloBoardIdTextField, 1, false)
                .addLabeledComponent(new JBLabel("Enter Due time hours after submit"), trelloDueIntervalTimeTextField, 1, false)
                .addComponent(testTrelloConfigurationButton)
                .addLabeledComponent(testTrelloConfigurationButton, trelloSettingStatusLabel, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    private void doTrelloTestConnection() {
        trelloSettingStatusLabel.setVisible(true);
        TrelloConfiguration trelloConfiguration = this.getCurrentTrelloConfiguration();
        CodeReviewBoardService codeReviewBoardService = new CodeReviewBoardService(trelloConfiguration);
        try {
            codeReviewBoardService.getTrelloBoardMembers();
            trelloSettingStatusLabel.setForeground(Color.GREEN);
            trelloSettingStatusLabel.setText(CONNECTION_SUCCESS_TIPS);
        } catch (Exception exception) {
            trelloSettingStatusLabel.setForeground(Color.RED);
            if (exception.getMessage().equals(BOARD_ID_INVALID_ERROR_MESSAGE)) {
                trelloSettingStatusLabel.setText(INVALID_BOARD_ID_NOTIFICATION);
            } else if (exception instanceof NotAuthorizedException) {
                trelloSettingStatusLabel.setText(AUTHORIZED_FAILED_NOTIFICATION);
            } else {
                trelloSettingStatusLabel.setText(exception.getMessage());
            }
        }
    }


    public JPanel getPanel() {
        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return trelloApiKeyTextField;
    }

    public void setTrelloApiKey(String trelloApiKey) {
        this.trelloApiKeyTextField.setText(trelloApiKey);
    }

    public void setTrelloApiToken(String trelloApiToken) {
        this.trelloApiTokenTextField.setText(trelloApiToken);
    }

    public void setTrelloBoardId(String trelloBoardId) {
        this.trelloBoardIdTextField.setText(trelloBoardId);
    }

    public void setTrelloSettingStatusLabel(String trelloSettingStatusLabel) {
        this.trelloSettingStatusLabel.setText(trelloSettingStatusLabel);
    }

    public void setTrelloDueIntervalTime(String dueIntervalTimeText) {
        this.trelloDueIntervalTimeTextField.setText(dueIntervalTimeText);
    }

    public TrelloConfiguration getCurrentTrelloConfiguration() {
        return TrelloConfiguration.builder()
                .trelloApiKey(trelloApiKeyTextField.getText())
                .trelloApiToken(trelloApiTokenTextField.getText())
                .trelloBoardId(trelloBoardIdTextField.getText())
                .dueIntervalHours(Integer.parseInt(trelloDueIntervalTimeTextField.getText()))
                .build();
    }

}
