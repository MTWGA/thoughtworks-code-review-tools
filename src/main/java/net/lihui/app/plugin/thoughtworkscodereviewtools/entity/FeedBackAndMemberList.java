package net.lihui.app.plugin.thoughtworkscodereviewtools.entity;

import com.julienvey.trello.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FeedBackAndMemberList {
    private String feedback;
    private List<Member> memberList;
}
