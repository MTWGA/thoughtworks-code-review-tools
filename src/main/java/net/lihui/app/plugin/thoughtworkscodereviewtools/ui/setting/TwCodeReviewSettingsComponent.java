// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// lihui change the file start 2021

package net.lihui.app.plugin.thoughtworkscodereviewtools.ui.setting;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloConfiguration;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */

public class TwCodeReviewSettingsComponent {

    private final JPanel mainPanel;
    private final JBTextField trelloApiKeyTextField = new JBTextField();
    private final JBTextField trelloApiTokenTextField = new JBTextField();
    private final JBTextField trelloBoardIdTextField = new JBTextField();
    private final JBLabel trelloSettingStatusLabel = new JBLabel("");
    private final JBTextField trelloDueIntervalTimeTextField = new JBTextField("24");

    public TwCodeReviewSettingsComponent() {
        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Enter trello key: "), trelloApiKeyTextField, 1, false)
                .addLabeledComponent(new JBLabel("Enter trello token: "), trelloApiTokenTextField, 1, false)
                .addLabeledComponent(new JBLabel("Enter trello code review board: "), trelloBoardIdTextField, 1, false)
                .addLabeledComponent(new JBLabel("Enter Due time hours after submit"), trelloDueIntervalTimeTextField, 1, false)
                .addLabeledComponent(new JBLabel("Setting status: "), trelloSettingStatusLabel, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
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

    public TrelloConfiguration getCurrentTrelloConfiguration() {
        return TrelloConfiguration.builder()
                .trelloApiKey(trelloApiKeyTextField.getText())
                .trelloApiToken(trelloApiTokenTextField.getText())
                .trelloBoardId(trelloBoardIdTextField.getText())
                .dueIntervalHours(Integer.parseInt(trelloDueIntervalTimeTextField.getText()))
                .build();
    }

}
