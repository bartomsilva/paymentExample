package com.picpaysimplificado.picpaysimplificado.infra;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnAuthorizedException extends ResponseStatusException {
    public UnAuthorizedException (String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }

    public UnAuthorizedException() {
        this("Autorização negada");
    }
}