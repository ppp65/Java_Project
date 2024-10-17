package com.example.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import com.example.project.service.GPTChatBot;

@RestController
public class ChatController {

    @PostMapping("/chat")
    public ResponseEntity<String> chatWithGPT(@RequestBody String userInput) {
        GPTChatBot gptChatBot = new GPTChatBot();
        JSONObject inputJson = new JSONObject(userInput);
        String userMessage = inputJson.getString("message");
        String gptResponse = gptChatBot.getGPTResponse(userMessage);

        JSONObject responseJson = new JSONObject();
        responseJson.put("response", gptResponse);

        return ResponseEntity.ok(responseJson.toString());
    }
}
