package com.nat.guildapi.quest.internal;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.ErrorResponseException;

import java.util.HashMap;
import java.util.Map;

class QuestUpdateRequestNotValidException extends ErrorResponseException {
    public QuestUpdateRequestNotValidException(BindingResult result) {
        super(HttpStatus.BAD_REQUEST);

        this.setTitle("Quest Update Request Not Valid");
        this.setDetail("Quest Update Request Not Valid");

        Map<String, Object> errors = new HashMap<>();
        result.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        this.getBody().setProperty("errors", errors);
    }
}
