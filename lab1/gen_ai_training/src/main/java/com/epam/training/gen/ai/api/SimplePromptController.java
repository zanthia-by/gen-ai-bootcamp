package com.epam.training.gen.ai.api;

import com.epam.training.gen.ai.api.model.Response;
import com.epam.training.gen.ai.semantic.SemanticKernelPromptService;
import com.epam.training.gen.ai.semantic.SimplePromptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
public class SimplePromptController {

    private SimplePromptService azurePromptService;
    private SemanticKernelPromptService semanticKernelPromptService;

    SimplePromptController(SimplePromptService azurePromptService,
                           SemanticKernelPromptService semanticKernelPromptService) {
        this.azurePromptService = azurePromptService;
        this.semanticKernelPromptService = semanticKernelPromptService;
    }

    @GetMapping("/chat1")
    @ResponseBody
    ResponseEntity<Response> azurePrompt(@RequestParam(value = "prompt") String prompt) {
        var res = azurePromptService.getChatCompletions(prompt).stream()
                .collect(Collectors.joining("\n"));
        return new ResponseEntity<>(
                new Response(res),
                HttpStatus.OK);
    }

    @GetMapping("/chat2")
    @ResponseBody
    ResponseEntity<Response> semanticKernelPrompt(@RequestParam(value = "prompt") String prompt) {
        var res = semanticKernelPromptService.getChatCompletions(prompt);
        return new ResponseEntity<>(
                new Response(res),
                HttpStatus.OK);
    }

    @GetMapping("/chat3")
    @ResponseBody
    ResponseEntity<Response> azurePromptLimited(@RequestParam(value = "prompt") String prompt) {
        var res = azurePromptService.getChatCompletionsLimited(prompt).stream()
                .collect(Collectors.joining("\n"));
        return new ResponseEntity<>(
                new Response(res),
                HttpStatus.OK);
    }

}
