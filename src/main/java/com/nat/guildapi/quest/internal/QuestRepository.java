package com.nat.guildapi.quest.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface QuestRepository extends JpaRepository<QuestEntity, UUID> {
}
