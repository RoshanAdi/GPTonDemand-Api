package com.gpt.gptplus1.Service.WebSecurity;


import com.gpt.gptplus1.Entity.User;
import com.gpt.gptplus1.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            User user = userRepo.findByEmail(email);
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));

        }catch (UsernameNotFoundException e){return null;}

   }
   public boolean isUserEnabled(String email){
       User user = userRepo.findByEmail(email);
         return user != null && user.isEnabled();
   }
    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
        return authorities;
    }

    public User loadUser4Reset(String email){
        return userRepo.findByEmail(email);
    }

}
