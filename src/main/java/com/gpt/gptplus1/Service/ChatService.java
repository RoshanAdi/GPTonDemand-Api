package com.gpt.gptplus1.Service;
import com.gpt.gptplus1.Entity.ChatMsg;
import com.gpt.gptplus1.Entity.User;
import com.gpt.gptplus1.Repository.ChatMsgRepo;
import com.gpt.gptplus1.Repository.UserRepo;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class ChatService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    ChatMsgRepo chatMsgRepo;




    public ChatMsg ChatService2(User user1, ChatMsg chatMsg) {

        User user = userRepo.findByEmail(user1.getEmail());
        List<ChatMsg> chatMsgs = user.getChatHistory();
        chatMsg.setUser(user1);
        chatMsgs.add(chatMsg);
        if (chatMsg.getMaxHistory()<150){chatMsg.setMaxHistory(150);}
        chatMsgs = limitChatHistoryLength(user1,chatMsg,chatMsg.getMaxHistory());
        String token = user.getApiKey();
        OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));
        System.out.println("Streaming chat completion...");
        final List<ChatMessage> messages = new ArrayList<>();

        for (ChatMsg chatMsg1 : chatMsgs) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setContent(chatMsg1.getContent());
            chatMessage.setRole(chatMsg1.getRole());
            messages.add(chatMessage);
        }
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), chatMsg.getSystemMassage());
        if (chatMsg.getSystemMassage() != null && chatMsg.getSystemMassage().length()>2){messages.add(systemMessage);}
        if (chatMsg.getModel()==null ){chatMsg.setModel("gpt-4");}
        if (chatMsg.getMaxTokens()<2){chatMsg.setMaxTokens(50);}
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model(chatMsg.getModel())
                .messages(messages)
                .n(1)
                .maxTokens(chatMsg.getMaxTokens())
                .logitBias(new HashMap<>())
                .build();

        ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
        ChatMsg chatMsg1 = new ChatMsg();
        chatMsg1.setContent(responseMessage.getContent());
        chatMsg1.setRole(responseMessage.getRole());
        chatMsg1.setUser(user);
        chatMsgs.add(chatMsg1);
        user.setChatHistory(chatMsgs);
userRepo.save(user);
        messages.add(responseMessage);
        for (ChatMessage message : messages) {
            System.out.println(message);
        }

       // service.shutdownExecutor();
        return chatMsg;
    }
    public List<ChatMsg> limitChatHistoryLength(User user, ChatMsg currentMsg, int maxLength) {
        List<ChatMsg> chatMsgs = user.getChatHistory();

        // Calculate the current total length of chatMsgs including the current and system messages
        int currentLength = chatMsgs.stream()
                .mapToInt(msg -> msg.getContent().length())
                .sum();

        // Add the length of the current message
        currentLength += currentMsg.getContent().length();

        // Add the length of the system message
       if (currentMsg.getSystemMassage() != null) {
            currentLength += currentMsg.getSystemMassage().length();
        }

        // Check if the current length exceeds the limit
        while (currentLength > maxLength && !chatMsgs.isEmpty()) {
            // Remove the earliest message
            ChatMsg removedMsg = chatMsgs.remove(0);
            currentLength -= removedMsg.getContent().length();
        }

        return  chatMsgs;
    }
    }
