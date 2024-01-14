package com.banco.santander.clients;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Component
public class NotificationClient {

    private final RestTemplate restTemplate;

    @Value("${notificationServiceUrl}")
    private String notificationServiceUrl;

    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendMessage() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(notificationServiceUrl, HttpMethod.GET, entity, String.class);
            log.info("Notification sent successfully");
        } catch (Exception e) {
            log.error("Error sending notification: {}", e.getMessage());
        }
    }
}
