package com.epam.training.gen.ai.semantic;

import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestFunctionMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.epam.training.gen.ai.semantic.client.OpenAIAsyncClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SimplePromptService {

    public static final String GREETING_MESSAGE = "Say hi";

    private final OpenAIAsyncClientService aiClientService;
    private final String deploymentOrModelName;

    public SimplePromptService(OpenAIAsyncClientService aiClientService,
                               @Value("${client-azureopenai-deployment-name}")  String deploymentOrModelName) {
        this.aiClientService = aiClientService;
        this.deploymentOrModelName = deploymentOrModelName;
    }

    public List<String> getChatCompletions(String prompt) {
        ChatCompletions completions = aiClientService.get()
                .getChatCompletions(
                        deploymentOrModelName,
                        new ChatCompletionsOptions(
                                List.of(new ChatRequestUserMessage(prompt))))
                .block();
        List<String> messages = completions.getChoices().stream()
                .map(c -> c.getMessage().getContent())
                .collect(Collectors.toList());
        log.info(messages.toString());
        return messages;
    }

    public List<String> getChatCompletionsLimited(String input) {
        ChatRequestFunctionMessage functionMessage = new ChatRequestFunctionMessage("Limit",
                String.format("Include no more than top 3 answers"));

        ChatCompletions completions = aiClientService.get()
                .getChatCompletions(
                        deploymentOrModelName,
                        new ChatCompletionsOptions(
                                List.of(new ChatRequestUserMessage(input),
                                        functionMessage)))
                .block();

        List<String> messages = completions.getChoices().stream()
                .map(c -> c.getMessage().getContent())
                .collect(Collectors.toList());
        log.info(messages.toString());
        return messages;
    }
}


