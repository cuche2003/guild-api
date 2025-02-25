package com.nat.guildapi.quest.internal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
class QuestEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Quest title must not be empty")
    @Size(max = 127, message = "Quest title must not exceed 127 characters")
    private String title;

    @NotBlank(message = "Quest description must not be empty")
    @Size(min = 15, max = 511, message = "Quest description must be between 15 and 511 characters")
    private String description;
}
