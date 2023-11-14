package com.gpt.gptplus1.Controller;

import com.gpt.gptplus1.Entity.ResponseMessage;
import com.gpt.gptplus1.Entity.User;
import com.gpt.gptplus1.Service.Email.VerificationEmail;
import com.gpt.gptplus1.Service.WebSecurity.AuthToken;
import com.gpt.gptplus1.Service.WebSecurity.TokenProvider;
import com.gpt.gptplus1.Service.WebSecurity.UserDetailsServiceImpl;
import com.gpt.gptplus1.Service.WebSecurity.UserService;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


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
    private VerificationEmail verificationEmail;
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
            // Check if the user is enabled

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

    @GetMapping("/api1/verify")

    public RedirectView verifyUser(@Param("code") String code) {
        if (verificationEmail.verify(code)) {
            return new RedirectView(clientURL+"reg-success");

        } else {
            return new RedirectView(clientURL+"reg-fail");
        }
    }

}
