package com.picpaysimplificado.picpaysimplificado.infra;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ServiceUnavailableException extends ResponseStatusException {
    public ServiceUnavailableException(String reason) {
        super(HttpStatus.SERVICE_UNAVAILABLE, reason);
    }
}

