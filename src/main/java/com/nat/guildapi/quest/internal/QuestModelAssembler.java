package com.nat.guildapi.quest.internal;

import com.nat.guildapi.quest.QuestController;
import com.nat.guildapi.quest.QuestDto;
import jakarta.annotation.Nonnull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class QuestModelAssembler implements RepresentationModelAssembler<QuestDto, EntityModel<QuestDto>> {
    @Override
    @Nonnull
    public EntityModel<QuestDto> toModel(@Nonnull QuestDto questDto) {
        return EntityModel.of(questDto, //
            linkTo(methodOn(QuestController.class).one(questDto.getQuestId())).withSelfRel(),
            linkTo(methodOn(QuestController.class).all(null)).withRel("quests"));
    }
}
