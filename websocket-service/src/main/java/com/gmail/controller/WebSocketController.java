package com.gmail.controller;

import com.gmail.constants.PathConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.API_V1_WEBSOCKET)
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public void send(@RequestParam("destination") String destination, @RequestBody Object request) {
        messagingTemplate.convertAndSend(destination, request);
    }
}
