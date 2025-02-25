package com.nat.guildapi.quest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Quest", description = "Manage quests")
@RestController
@RequestMapping("/api/v1/quests")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin("*")
public interface QuestController {

    @Operation(summary = "Get all quests")
    @GetMapping("")
    CollectionModel<EntityModel<QuestDto>> all(Pageable pageable);

    @Operation(summary = "Create a quest")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<QuestDto>> create(
        @AuthenticationPrincipal Jwt principal,
        @Valid @RequestBody QuestCreateRequestDto requestDto,
        BindingResult result
    );

    @Operation(summary = "Get a quest")
    @GetMapping("/{id}")
    EntityModel<QuestDto> one(@PathVariable UUID id);

    @Operation(summary = "Update a quest")
    @PutMapping("/{id}")
    ResponseEntity<EntityModel<QuestDto>> update(
        @PathVariable UUID id,
        @Valid @RequestBody QuestUpdateRequestDto requestDto,
        BindingResult result
    );

    @Operation(summary = "Delete a quest with id")
    @DeleteMapping("/{id}")
    ResponseEntity<EntityModel<QuestDto>> delete(@PathVariable UUID id);
}
