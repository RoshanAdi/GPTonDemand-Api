package com.gpt.gptplus1.Repository;


import com.gpt.gptplus1.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Repository
@Component
public interface UserRepo extends JpaRepository<User,Integer>{

        public User findByEmail(String email);
        @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
        public User findByVerificationCode(String code);
}
