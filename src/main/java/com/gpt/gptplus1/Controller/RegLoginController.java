package com.gpt.gptplus1.Controller;

import com.gpt.gptplus1.Entity.ResponseMessage;
import com.gpt.gptplus1.Entity.User;
import com.gpt.gptplus1.Service.WebSecurity.AuthToken;
import com.gpt.gptplus1.Service.WebSecurity.TokenProvider;
import com.gpt.gptplus1.Service.WebSecurity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
public class RegLoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider jwtTokenUtil;
    @Autowired
    private UserService userService;

    @PostMapping(value="/api1/register" )
    public ResponseMessage saveUser(@RequestBody User user) {
        String message="";
        if (userService.getUserByEmail(user.getEmail())!=null) {

            message = user.getEmail()+" is already registered!. May be you haven't verify your email address. if so, please check your mailbox  ";
            return new ResponseMessage(message);
        } else {
            userService.saveUser(user);
            message = "Please confirm your email address. We have sent an confirmation link to "+user.getEmail();
            return new ResponseMessage(message);
        }

    }
    @PostMapping(value = "/api1/login")
    public ResponseEntity<?> generateToken(@RequestBody User loginUser) throws AuthenticationException {
        System.out.println("user details got "+loginUser.getEmail()+"  pass "+loginUser.getPassword());
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getPassword()
                )
        );
        System.out.println("sending token to client ");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        System.out.println("sending token to client "+token);
        return ResponseEntity.ok(new AuthToken(token));
    }



}
