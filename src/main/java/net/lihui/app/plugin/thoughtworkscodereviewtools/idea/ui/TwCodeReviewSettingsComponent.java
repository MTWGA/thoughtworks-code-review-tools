// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
// lihui change the file start 2021

package net.lihui.app.plugin.thoughtworkscodereviewtools.idea.ui;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import net.lihui.app.plugin.thoughtworkscodereviewtools.idea.store.TrelloConfiguration;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class TwCodeReviewSettingsComponent {

    private final JPanel myMainPanel;
    private final JBTextField trelloApiKey = new JBTextField();
    private final JBTextField trelloApiToken = new JBTextField();
    private final JBTextField trelloBoardId = new JBTextField();

    public TwCodeReviewSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Enter trello key: "), trelloApiKey, 1, false)
                .addLabeledComponent(new JBLabel("Enter trello token: "), trelloApiToken, 1, false)
                .addLabeledComponent(new JBLabel("Enter trello code review board: "), trelloBoardId, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }


    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return trelloApiKey;
    }

    public String getTrelloApiKey() {
        return trelloApiKey.getText();
    }

    public void setTrelloApiKey(String trelloApiKey) {
        this.trelloApiKey.setText(trelloApiKey);
    }

    public String getTrelloApiToken() {
        return trelloApiToken.getText();
    }

    public void setTrelloApiToken(String trelloApiToken) {
        this.trelloApiToken.setText(trelloApiToken);
    }

    public String getTrelloBoardId() {
        return trelloBoardId.getText();
    }

    public void setTrelloBoardId(String trelloBoardId) {
        this.trelloBoardId.setText(trelloBoardId);
    }

    public boolean isConfigurationChanged(TrelloConfiguration trelloConfiguration) {
        return !trelloApiKey.equals(trelloConfiguration.getTrelloApiKey())
                || !trelloApiToken.equals(trelloConfiguration.getTrelloApiToken())
                || !trelloBoardId.equals(trelloConfiguration.getTrelloBoardId());
    }
}
