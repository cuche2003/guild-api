package com.nat.guildapi.quest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Adventure Service
 */
@Service
public interface QuestService {
    Page<QuestDto> getAllQuests(Pageable pageable);

    QuestDto getQuestById(UUID id);

    QuestDto createQuest(QuestCreateRequestDto request);

    QuestDto updateQuest(UUID id, QuestUpdateRequestDto request);

    QuestDto deleteQuest(UUID id);
}
