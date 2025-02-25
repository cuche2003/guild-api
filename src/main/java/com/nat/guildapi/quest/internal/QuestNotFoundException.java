package com.nat.guildapi.quest.internal;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

import java.util.UUID;

class QuestNotFoundException extends ErrorResponseException {
    public QuestNotFoundException(UUID id) {
        super(HttpStatus.NOT_FOUND);

        this.setTitle("Quest Not Found");
        this.setDetail("Quest with id " + id + "is not found");
    }
}
