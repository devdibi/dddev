package com.dddev.log.controller;

import com.dddev.log.dto.ChatGptReq;
import com.dddev.log.exception.ChatGptException;
import com.dddev.log.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class gptcontroller {

    private final ChatService chatService;

    @PostMapping("/log")
    public String logChat(@RequestBody ChatGptReq question) {
        if(question.getQuestion().equals("")){
            throw new ChatGptException.IncorrectQuestion("질문을 입력해주세요.");
        }
        return chatService.getChatResponse(question.getQuestion());
    }

    @GetMapping("/log")
    public String test(@RequestBody ChatGptReq question) {
        if(question.getQuestion().equals("")){
            throw new ChatGptException.IncorrectQuestion("질문을 입력해주세요.");
        }
        return chatService.getChatResponse(question.getQuestion());
    }
}
