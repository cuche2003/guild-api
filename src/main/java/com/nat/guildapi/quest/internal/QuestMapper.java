package com.nat.guildapi.quest.internal;

import com.nat.guildapi.quest.QuestCreateRequestDto;
import com.nat.guildapi.quest.QuestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
interface QuestMapper {
    @Mappings(value = {
        @Mapping(source = "id", target = "questId"),
        @Mapping(source = "title", target = "questTitle"),
        @Mapping(source = "description", target = "questDescription")
    })
    QuestDto questEntityToQuestDto(QuestEntity questEntity);

    @Mappings(value = {
        @Mapping(target = "id", ignore = true),
        @Mapping(source = "questTitle", target = "title"),
        @Mapping(source = "questDescription", target = "description")
    })
    QuestEntity questCreateRequestDtoToQuestEntity(
        QuestCreateRequestDto questCreateRequestDto
    );
}

