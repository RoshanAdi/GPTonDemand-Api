package com.gpt.gptplus1.Repository;

import com.gpt.gptplus1.Entity.ChatMsg;
import com.gpt.gptplus1.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface ChatMsgRepo extends JpaRepository<ChatMsg , Integer> {
    List<ChatMsg> findByUser(User user);

}
