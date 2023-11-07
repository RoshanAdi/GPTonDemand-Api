package com.gpt.gptplus1.Controller;

import org.springframework.beans.factory.annotation.Value;

public class ChatController {

    @Value("${my.property}")
    private String myProperty;

}
