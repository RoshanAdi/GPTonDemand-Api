package com.gpt.gptplus1.Service.WebSecurity;

import com.gpt.gptplus1.Entity.User;
import com.gpt.gptplus1.Repository.UserRepo;
import com.gpt.gptplus1.Service.Email.SendEmail;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    SendEmail sendEmail;


public User getUserByEmail(String email){

    return userRepo.findByEmail(email);
}
    public void saveUser(User user,String siteURL){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("user");
            user.setEnabled(false);
            String randomCode = RandomString.make(64);
            user.setVerificationCode(randomCode);
            sendEmail.sendVerificationEmail(user, siteURL);
            userRepo.save(user);
        }
        catch (Exception e){
            e.toString();
        }
    }

}
