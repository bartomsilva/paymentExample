package com.picpaysimplificado.picpaysimplificado.services;

import com.picpaysimplificado.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.picpaysimplificado.infra.ServiceUnavailableException;
import com.picpaysimplificado.picpaysimplificado.infra.UnAuthorizedException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AuthorizationService {

    @Autowired
    RestTemplate restTemplate;

    public void authorizeTransaction(User sender, BigDecimal value) throws Exception {

        // Requisição para autorização da transação
        ResponseEntity<Map> authorizationResponse;
        try {
            authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        } catch( Exception e){
            throw new ServiceUnavailableException("o servidor de autorização não respondeu");
        }

        // Verificar se a resposta é nula
        Map responseBody = authorizationResponse.getBody();
        if (responseBody == null) {
            throw new ServiceUnavailableException("O servidor não respondeu, tente novamente mais tarde.");
        }

        // Extrair status da resposta
        String status = (String) responseBody.get("status");
        if (!"success".equals(status)) {
            throw new UnAuthorizedException("Falha na autorização da transação.");
        }

        // Extrair dados da resposta
        Map<String, Object> dataMap = (Map<String, Object>) responseBody.get("data");
        if (dataMap == null || dataMap.get("authorization") == null) {
            throw new UnAuthorizedException("Falha na transação.");
        }

        Boolean authorization = (Boolean) dataMap.get("authorization");
        if (!authorization) {
            throw new UnAuthorizedException("transação não autorizada");
        }
    }
}
