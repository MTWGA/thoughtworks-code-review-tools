package net.lihui.app.plugin.thoughtworkscodereviewtools.mapper;

import com.julienvey.trello.domain.Member;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMember;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerCheckboxDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MemberMapper {
    MemberMapper MEMBER_MAPPER = Mappers.getMapper(MemberMapper.class);

    TrelloBoardMember toState(Member member);

    List<TrelloBoardMember> toStateList(List<Member> members);

    OwnerCheckboxDTO toDto(TrelloBoardMember trelloBoardMember);

    Member toMember(OwnerCheckboxDTO ownerCheckboxDTO);

    List<OwnerCheckboxDTO> toDtoList(List<TrelloBoardMember> trelloBoardMember);

    List<Member> toMemberList(List<OwnerCheckboxDTO> ownerCheckboxDTO);
}
