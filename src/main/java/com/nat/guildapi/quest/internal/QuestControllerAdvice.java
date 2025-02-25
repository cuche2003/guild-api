package com.nat.guildapi.quest.internal;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class QuestControllerAdvice extends ResponseEntityExceptionHandler {
    protected ResponseEntity<ProblemDetail> createResponseEntity(ErrorResponseException ex) {
        return new ResponseEntity<>(ex.getBody(), ex.getHeaders(), ex.getStatusCode());
    }

    @ExceptionHandler(QuestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<ProblemDetail> handleQuestNotFound(QuestNotFoundException ex) {
        return createResponseEntity(ex);
    }

    @ExceptionHandler(QuestCreateRequestNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ProblemDetail> handleQuestCreateRequestNotValid(
            QuestCreateRequestNotValidException ex
    ) {
        return createResponseEntity(ex);
    }

    @ExceptionHandler(QuestUpdateRequestNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ProblemDetail> handleQuestUpdateRequestNotValid(
            QuestUpdateRequestNotValidException ex
    ) {
        return createResponseEntity(ex);
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<>(
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.FORBIDDEN,
                        "You do not have permission to perform this operation!"
                ),
                new HttpHeaders(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ProblemDetail> handleAuthentication(AuthenticationException ex) {
        return new ResponseEntity<>(
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.UNAUTHORIZED,
                        "You are not authenticated!"
                ),
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED
        );
    }
}