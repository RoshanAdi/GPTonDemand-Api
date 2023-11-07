package com.gpt.gptplus1;

import com.gpt.gptplus1.Service.ChatService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GptPlus1Application {

    public static void main(String[] args) {
        SpringApplication.run(GptPlus1Application.class, args);
        new ChatService();
    }

}
