package com.epam.training.gen.ai.semantic;

import com.epam.training.gen.ai.semantic.client.OpenAIAsyncClientService;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SemanticKernelPromptService {

    private Kernel kernel;

    public SemanticKernelPromptService(OpenAIAsyncClientService aiClientService,
                                       @Value("${client-azureopenai-deployment-name}")  String deploymentOrModelName) {
        this.kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, ChatCompletionService.builder()
                        .withModelId(deploymentOrModelName)
                        .withOpenAIAsyncClient(aiClientService.get())
                        .build())
                .build();
    }

    public String getChatCompletions(String prompt) {
        return kernel.invokePromptAsync(prompt).block().getResult().toString();
    }

}


