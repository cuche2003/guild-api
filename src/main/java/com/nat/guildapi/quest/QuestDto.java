package com.nat.guildapi.quest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;

/**
 * Represents a Quest
 */
@Data
@Relation(value = "quest", collectionRelation = "quests")
public class QuestDto {
    @NotBlank(message = "Quest id cannot be empty")
    private UUID questId;

    @NotBlank(message = "Quest title must not be empty")
    @Size(max = 127, message = "Quest title cannot exceed 127 characters")
    private String questTitle;

    @NotBlank(message = "Quest description must not be empty")
    @Size(min = 15, max = 511, message = "Quest description must be between 15 and 511 characters")
    private String questDescription;
}
