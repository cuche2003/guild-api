package com.nat.guildapi.quest.internal;

import com.nat.guildapi.quest.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/quests")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin("*")
class BaseQuestController implements QuestController {
    private final QuestService questService;
    private final QuestModelAssembler questModelAssembler;
    private final PagedResourcesAssembler<QuestDto> pagedResourcesAssembler;

    public BaseQuestController(
        QuestService questService,
        QuestModelAssembler questModelAssembler,
        PagedResourcesAssembler<QuestDto> pagedResourcesAssembler
    ) {
        this.questService = questService;
        this.questModelAssembler = questModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    @GetMapping("")
    public CollectionModel<EntityModel<QuestDto>> all(
        @ParameterObject @PageableDefault(size = 5, sort = "title") Pageable pageable
    ) {
        return pagedResourcesAssembler.toModel(questService.getAllQuests(pageable), questModelAssembler);
    }

    @Override
    @PostMapping(
        value = "",
        consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE
        }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('create:quests')")
    public ResponseEntity<EntityModel<QuestDto>>
    create(
        @AuthenticationPrincipal Jwt principal,
        @Valid @RequestBody QuestCreateRequestDto requestDto,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new QuestCreateRequestNotValidException(result);
        }

        QuestDto questDto = questService.createQuest(requestDto);
        return ResponseEntity
            .created(linkTo(methodOn(BaseQuestController.class).one(questDto.getQuestId())).toUri())
            .body(questModelAssembler.toModel(questDto));

    }

    @Override
    @GetMapping("/{id}")
    public EntityModel<QuestDto> one(@PathVariable UUID id) {
        QuestDto questDto = questService.getQuestById(id);
        return questModelAssembler.toModel(questDto);
    }

    @Override
    @PutMapping(
        value = "/{id}",
        consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE
        }
    )
    @PreAuthorize("hasAuthority('update:quests')")
    public ResponseEntity<EntityModel<QuestDto>> update(
        @PathVariable UUID id,
        @Valid @RequestBody QuestUpdateRequestDto requestDto, BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new QuestUpdateRequestNotValidException(result);
        }

        QuestDto questDto = questService.updateQuest(id, requestDto);
        return ResponseEntity.ok(questModelAssembler.toModel(questDto));
    }

    @Override
    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize("hasAuthority('delete:quests')")
    public ResponseEntity<EntityModel<QuestDto>> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(questModelAssembler.toModel(questService.deleteQuest(id)));
    }
}
