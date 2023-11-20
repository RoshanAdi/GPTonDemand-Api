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
import java.util.Random;

@Service
public class SendEmail {

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
        String verifyURL = siteURL + "/api1/verify-account?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }
    public void sendCodes(User user, String siteURL) throws UnsupportedEncodingException, MessagingException {
        String toAddress = user.getEmail();
        String fromAddress = "smsemail83@gmail.com";
        String senderName = "GPTPlus";
        String subject = "Your verification code";
        String content = "Dear [[name]],<br>"
                + "your one-time verification code is <span style=\"background-color: yellow; font-weight: bold;\">[[code]]</span>:<br>"
                + "Thank you,<br>"
                + "GPTPlus.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getFirstName());
        String randomNumber = generateRandomNumericString();
        user.setVerificationCode(randomNumber);
        userRepo.save(user);
        content = content.replace("[[code]]", randomNumber);

        helper.setText(content, true);

        mailSender.send(message);
    }
    private static String generateRandomNumericString() {
        Random random = new Random();
        StringBuilder numericString = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            numericString.append(digit);
        }

        return numericString.toString();
    }
    public boolean verify(String verificationCode) {
        User user = userRepo.findByVerificationCode(verificationCode);
        if (user == null || user.isEnabled()) {
            return false;
        }
        else {
            user.setEnabled(true);
            userRepo.save(user);
            return true;
        }

    }
}
