package net.lihui.app.plugin.thoughtworkscodereviewtools.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    private String id;
    private String name;
    private String idList;
    private String desc;
    private Date due;
    private List<String> idMembers;
    private List<String> idLabels;
}
