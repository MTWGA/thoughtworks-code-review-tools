package net.lihui.app.plugin.thoughtworkscodereviewtools.ui;

import com.julienvey.trello.domain.Label;
import com.julienvey.trello.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FeedbackContext {
    private String feedback;
    private Member member;
    private Label label;
    private String description;
}
