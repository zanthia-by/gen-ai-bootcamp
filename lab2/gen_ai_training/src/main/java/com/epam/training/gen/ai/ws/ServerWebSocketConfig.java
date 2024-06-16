package com.epam.training.gen.ai.ws;

import com.epam.training.gen.ai.semantic.SemanticKernelPromptService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class ServerWebSocketConfig implements WebSocketConfigurer {

    private SemanticKernelPromptService semanticKernelPromptService;

    ServerWebSocketConfig(SemanticKernelPromptService semanticKernelPromptService) {
        this.semanticKernelPromptService = semanticKernelPromptService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/chat");
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new ServerWebSocketHandler(semanticKernelPromptService);
    }
}