package net.lihui.app.plugin.thoughtworkscodereviewtools.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class TrelloRequestErrorConstant {
    public static final String BOARD_ID_INVALID_ERROR_MESSAGE = "invalid id";

    public static final String INVALID_BOARD_ID_NOTIFICATION = "trello board id is invalid，please check your configuration";
    public static final String AUTHORIZED_FAILED_FILL_SETTING_NOTIFICATION = "Can not access your trello board, please check your trello configuration in: Preferences -> TW Code Review Tools";
    public static final String SET_UP_NOTIFICATION = "Please fill your trello configuration in: Preferences -> TW Code Review Tools";
    public static final String AUTHORIZED_FAILED_NOTIFICATION = "authorized failed, please check your trello configuration";
}
