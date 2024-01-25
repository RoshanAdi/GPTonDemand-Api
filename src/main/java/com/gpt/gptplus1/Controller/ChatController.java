package com.gpt.gptplus1.Controller;
import com.gpt.gptplus1.Entity.User;
import com.gpt.gptplus1.Service.WebSecurity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



    @RestController
    @RequestMapping("/")
    public class ChatController {
        @Autowired
        private UserService userService;

        @GetMapping("/api2/profile")
        @PreAuthorize("hasRole('user')")
        public User sendUserProfile() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String user = authentication.getName();
            return userService.getUserByEmail(user);
        }

}
