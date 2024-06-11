package com.jsrspring.screenmatch.service;

import com.jsrspring.screenmatch.utils.config.Configuration;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ChatGPTApiService {
    public static String getTranslation(String text) {
        OpenAiService service = new OpenAiService(Configuration.OPEN_AI_API_KEY);

        CompletionRequest requisition = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("Traduce a español el siguiente texto: " + text)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var response = service.createCompletion(requisition);
        return response.getChoices().get(0).getText();
    }
}
