package com.nat.guildapi.quest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuestCreateRequestDto {
    @NotBlank(message = "Quest title must not be empty")
    @Size(max = 127, message = "Quest title cannot exceed 127 characters")
    private String questTitle;

    @NotBlank(message = "Quest description must not be empty")
    @Size(min = 15, max = 511, message = "Quest description must be between 15 and 511 characters")
    private String questDescription;
}
