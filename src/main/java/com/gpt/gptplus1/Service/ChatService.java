package com.gpt.gptplus1.Service;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.util.ArrayList;
import java.util.List;

public class ChatService {
    public ChatService() {

        //String apiKey = "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";
        String apiKey = "sk-SW7JgCrtK0OVQbxlusPWT3BlbkFJ3vE6LO1o7XXmj2qa3I";

        // Create an OpenAiService instance with your API key
        OpenAiService openAiService = new OpenAiService(apiKey);

        // Create a ChatCompletionRequest
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel("gpt-4");

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage("system", ""));
        chatMessages.add(new ChatMessage("user", "your gpt model?"));
        request.setMessages(chatMessages);

try {
    // Call the createChatCompletion method to get a ChatCompletionResult
    ChatCompletionResult chatCompletionResult = openAiService.createChatCompletion(request);

    // Print the assistant's reply
    System.out.println("Assistant's reply: " + chatCompletionResult.getChoices().get(0).getMessage().getContent());
}
catch (OpenAiHttpException e) {
    System.err.println("OpenAI API request failed with status code: " + e.getMessage());
    System.err.println("Error message: " + e.getMessage());
}
    }
}
