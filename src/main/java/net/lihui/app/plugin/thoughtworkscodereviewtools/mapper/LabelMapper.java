package net.lihui.app.plugin.thoughtworkscodereviewtools.mapper;

import com.julienvey.trello.domain.Label;
import net.lihui.app.plugin.thoughtworkscodereviewtools.intellij.store.TrelloBoardLabel;
import net.lihui.app.plugin.thoughtworkscodereviewtools.ui.dto.LabelDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface LabelMapper {
    LabelMapper LABEL_MAPPER = Mappers.getMapper(LabelMapper.class);

    List<TrelloBoardLabel> toLabelList(List<Label> labels);

    List<LabelDTO> toLabelDtoList(List<TrelloBoardLabel> trelloBoardLabels);

    Label toLabel(LabelDTO labelDTOS);
}
