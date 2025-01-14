package com.dvtsoftware.mealgen.aws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sns")
@Slf4j
public class SnsController {

    private final SnsService snsService;

    public SnsController(SnsService snsService) {
        this.snsService = snsService;
    }

    @PostMapping("/publish")
    public String publishToSns(@RequestParam String topicName,
                               @RequestParam(required = false) String subject,
                               @RequestBody String requestPayload) {
        try {
            log.info("Received request to publish message to topic: {}", topicName);
            snsService.publishToSns(topicName, requestPayload, subject != null ? subject : "Default Subject");
            log.info("Message successfully published to SNS topic: {}", topicName);
            return "Message successfully published to SNS topic: " + topicName;
        } catch (Exception e) {
            log.error("Failed to publish message to SNS topic: {}", topicName, e);
            return "Failed to publish message: " + e.getMessage();
        }
    }
}
