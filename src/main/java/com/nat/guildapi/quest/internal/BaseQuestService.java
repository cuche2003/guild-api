package com.nat.guildapi.quest.internal;

import com.nat.guildapi.quest.QuestCreateRequestDto;
import com.nat.guildapi.quest.QuestDto;
import com.nat.guildapi.quest.QuestService;
import com.nat.guildapi.quest.QuestUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class BaseQuestService implements QuestService {
    private final QuestRepository questRepository;
    private final QuestMapper questMapper;

    public BaseQuestService(
            QuestRepository questRepository,
            QuestMapper questMapper
    ) {
        this.questRepository = questRepository;
        this.questMapper = questMapper;
    }

    @Override
    public Page<QuestDto> getAllQuests(Pageable pageable) {
        return questRepository.findAll(pageable)
                .map(questMapper::questEntityToQuestDto);
    }

    @Override
    public QuestDto getQuestById(UUID id) {
        return questRepository.findById(id).map(questMapper::questEntityToQuestDto)
                .orElseThrow(() -> new QuestNotFoundException(id));
    }

    @Override
    @PreAuthorize("hasAuthority('create:quests')")
    public QuestDto createQuest(QuestCreateRequestDto request) {
        return questMapper.questEntityToQuestDto(
                questRepository.save(
                        questMapper.questCreateRequestDtoToQuestEntity(request)
                )
        );
    }

    @Override
    @PreAuthorize("hasAuthority('update:quests')")
    public QuestDto updateQuest(UUID id, QuestUpdateRequestDto request) {
        return questRepository.findById(id).map(
                quest -> {
                    quest.setTitle(request.getQuestTitle());
                    quest.setDescription(request.getQuestDescription());
                    return questMapper.questEntityToQuestDto(questRepository.save(quest));
                }
        ).orElseThrow(() -> new QuestNotFoundException(id));
    }

    @Override
    @PreAuthorize("hasAuthority('delete:quests')")
    public QuestDto deleteQuest(UUID id) {
        QuestDto questDto = getQuestById(id);
        questRepository.deleteById(id);
        return questDto;
    }
}
