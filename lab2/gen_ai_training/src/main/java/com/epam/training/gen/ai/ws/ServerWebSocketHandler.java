package com.epam.training.gen.ai.ws;

import com.epam.training.gen.ai.semantic.SemanticKernelPromptService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ServerWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, ChatHistory> idToHistory = new HashMap<>();

    private final SemanticKernelPromptService semanticKernelPromptService;

    ServerWebSocketHandler(SemanticKernelPromptService semanticKernelPromptService) {
        this.semanticKernelPromptService = semanticKernelPromptService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        ChatHistory chatHistory = semanticKernelPromptService.init();
        idToHistory.put(session.getId(), chatHistory);

        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String request = message.getPayload();

        ChatHistory chatHistory = idToHistory.get(session.getId());
        chatHistory.addUserMessage(request);
        logLastMessage(session.getId(), chatHistory);

        var response = semanticKernelPromptService.getCompletion(chatHistory);

        logLastMessage(session.getId(), chatHistory);

        session.sendMessage(new TextMessage(response));
    }

    private static void logLastMessage(String sessionId, ChatHistory chatHistory) {
        var message = chatHistory.getLastMessage().get();
        log.info("(" + sessionId + ") " + message.getAuthorRole() + ": " + message.getContent());
        log.info("------------------------");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        idToHistory.remove(session.getId());

        super.afterConnectionClosed(session, status);
    }

}
