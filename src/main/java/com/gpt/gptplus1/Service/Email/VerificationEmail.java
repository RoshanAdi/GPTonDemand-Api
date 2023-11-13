package com.gpt.gptplus1.Service.Email;

import com.gpt.gptplus1.Entity.User;
import com.gpt.gptplus1.Repository.UserRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
@Service
public class VerificationEmail {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    UserRepo userRepo;
    public void sendVerificationEmail(User user, String siteURL) throws UnsupportedEncodingException, MessagingException {
        String toAddress = user.getEmail();
        String fromAddress = "smsemail83@gmail.com";
        String senderName = "GPTPlus";
        String subject = "Please Verify Your Email Address";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your Email address:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Roshan.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName());
        String verifyURL = siteURL + "/api1/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public boolean verify(String verificationCode) {
        User user = userRepo.findByVerificationCode(verificationCode);
        if (user == null || user.isEnabled()) {
            return false;
        }
        else {
            user.setEnabled(true);
            userRepo.save(user);
           // student.setRole("Student");    //cannot log until role sets at this point.
          //  studentRepo.save(student);

            return true;
        }

    }
}
