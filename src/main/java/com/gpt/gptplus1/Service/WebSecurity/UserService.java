package com.gpt.gptplus1.Service.WebSecurity;

import com.gpt.gptplus1.Entity.User;
import com.gpt.gptplus1.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

public User getUserByEmail(String email){

    return userRepo.findByEmail(email);
}
    public String saveUser(User user){
        try {
            User newuser = user;
            newuser.setPassword(passwordEncoder.encode(user.getPassword()));
            newuser.setRole("customer");   // change as required or delete
            userRepo.save(newuser);
            System.out.println("encoded pw = "+newuser.getPassword());
            return "ok";
        }
        catch (Exception e){
            return e.toString();
        }
    }
}
