package com.picpaysimplificado.picpaysimplificado.infra;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConflictException extends ResponseStatusException {
    public ConflictException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }

    public ConflictException() {
        this("Recurso já cadastrado");
    }
}