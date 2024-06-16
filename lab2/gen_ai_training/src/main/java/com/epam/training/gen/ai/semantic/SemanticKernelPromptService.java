package com.epam.training.gen.ai.semantic;

import com.epam.training.gen.ai.semantic.client.OpenAIAsyncClientService;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.implementation.CollectionUtil;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SemanticKernelPromptService {
    private final ChatCompletionService chatCompletionService;

    public SemanticKernelPromptService(OpenAIAsyncClientService aiClientService,
                                       @Value("${client-azureopenai-deployment-name}") String deploymentOrModelName) {

        chatCompletionService = OpenAIChatCompletion.builder()
                .withModelId(deploymentOrModelName)
                .withOpenAIAsyncClient(aiClientService.get())
                .build();
    }

    public ChatHistory init() {
        return new ChatHistory("You are a librarian, expert about books");
    }

    public String getCompletion(ChatHistory chatHistory) {
        var executionSettings = PromptExecutionSettings.builder()
                .withMaxTokens(1000)
                .withTemperature(0)
                .build();

        var completion = chatGptReply(chatHistory, executionSettings);

        return completion;
    }


    private String chatGptReply(ChatHistory chatHistory, PromptExecutionSettings settings) {
        var invocationContext = InvocationContext.builder().withPromptExecutionSettings(settings)
                .build();

        var reply = chatCompletionService.getChatMessageContentsAsync(chatHistory, null, invocationContext);

        String message = reply
                .mapNotNull(CollectionUtil::getLastOrNull)
                .map(ChatMessageContent::getContent)
                .block();

        chatHistory.addAssistantMessage(message);

        return message;
    }

}


