package net.lihui.app.plugin.thoughtworkscodereviewtools.mapper;

import com.julienvey.trello.domain.Member;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardMember;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.OwnerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface MemberMapper {
    MemberMapper MEMBER_MAPPER = Mappers.getMapper(MemberMapper.class);

    TrelloBoardMember toState(Member member);

    List<TrelloBoardMember> toStateList(List<Member> members);

    OwnerDTO toDto(TrelloBoardMember trelloBoardMember);

    Member toMember(OwnerDTO ownerDTO);

    List<OwnerDTO> toDtoList(List<TrelloBoardMember> trelloBoardMember);
}
