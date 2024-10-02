package com.picpaysimplificado.picpaysimplificado.services;

import com.picpaysimplificado.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.picpaysimplificado.dtos.NotificationDTO;
import com.picpaysimplificado.picpaysimplificado.infra.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    public void senderNotification(User user, String message) throws Exception {
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email,message);
        ResponseEntity<String> notificationResponse = restTemplate
                .postForEntity("https://util.devi.tools/api/v1/notify",notificationRequest, String.class);
        if(notificationResponse.getStatusCode()!= HttpStatus.OK){
            throw new ServiceUnavailableException("serviço de notificação fora o ar, implementar serviço de mensageria");
        }
    }
}
