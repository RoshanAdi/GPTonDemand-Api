package com.gpt.gptplus1.Controller;

import com.gpt.gptplus1.Entity.ResponseMessage;
import com.gpt.gptplus1.Entity.User;
import com.gpt.gptplus1.Service.Email.SendEmail;
import com.gpt.gptplus1.Service.WebSecurity.AuthToken;
import com.gpt.gptplus1.Service.WebSecurity.TokenProvider;
import com.gpt.gptplus1.Service.WebSecurity.UserDetailsServiceImpl;
import com.gpt.gptplus1.Service.WebSecurity.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.util.Objects;


@RestController
@RequestMapping("/")
public class UserController {
    @Value("${frontEnd.URL}")
    private String clientURL;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider jwtTokenUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private SendEmail sendEmail;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @PostMapping(value="/api1/register" )
    public ResponseMessage saveUser(@RequestBody User user, HttpServletRequest request) {
        String message="";
        if (userService.getUserByEmail(user.getEmail())!=null) {

            message = user.getEmail()+" is already registered!. May be you haven't verify your email address. if so, please check your mailbox  ";
            return new ResponseMessage(message);
        } else {
            String siteURL = request.getRequestURL().toString();

            userService.saveUser(user,siteURL.replace(request.getServletPath(),""));
            message = "Please confirm your email address. We have sent an confirmation link to "+user.getEmail();
            return new ResponseMessage(message);
        }

    }
    @PostMapping(value = "/api1/login")
    public ResponseEntity<?> generateToken(@RequestBody User loginUser) throws AuthenticationException {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUser.getEmail(),
                            loginUser.getPassword()
                    )
            );

            if (!userDetailsService.isUserEnabled(loginUser.getEmail())) {

                return ResponseEntity.status(399)
                        .body("User is not enabled. Please verify your email.");
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenUtil.generateToken(authentication);


            return ResponseEntity.ok(new AuthToken(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    @PostMapping(value = "/api1/reset-password")
    public ResponseEntity<?> ResetPW(@RequestBody User loginUser, HttpServletRequest request) throws AuthenticationException {
        System.out.println(loginUser.getEmail());
        try {
            if (userDetailsService.loadUserByUsername(loginUser.getEmail())==null) {
                return ResponseEntity.status(399)
                        .body("");}
            User user = userDetailsService.loadUser4Reset(loginUser.getEmail());
            String siteURL = request.getRequestURL().toString();
            sendEmail.sendCodes(user,siteURL.replace(request.getServletPath(),"")); // remove this request
            return ResponseEntity.ok("");
        } catch (AuthenticationException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        } catch (MessagingException| UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping(value = "/api1/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody User loginUser) {
        String password = userDetailsService.loadUser4Reset(loginUser.getEmail()).getPassword();

        if (Objects.equals(loginUser.getVerificationCode(), userDetailsService.loadUser4Reset(loginUser.getEmail()).getVerificationCode())) {
            try {
                userService.saveNewPW(loginUser);
                final Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginUser.getEmail(),
                                loginUser.getPassword()
                        )
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                final String token = jwtTokenUtil.generateToken(authentication);

                return ResponseEntity.ok(new AuthToken(token));
            } catch (AuthenticationException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Error");
    }

    @GetMapping("/api1/verify-account") // for capturing email verification link

    public RedirectView verifyUser(@Param("code") String code) {
        if (sendEmail.verify(code)) {
            return new RedirectView(clientURL+"reg-success");

        } else {
            return new RedirectView(clientURL+"reg-fail");
        }
    }

}
