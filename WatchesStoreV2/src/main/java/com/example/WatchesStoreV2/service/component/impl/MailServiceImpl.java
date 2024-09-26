package com.example.WatchesStoreV2.service.component.impl;

import com.example.WatchesStoreV2.service.component.MailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public String sendMail(String to, String[] cc, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);

            javaMailSender.send(mimeMessage);

            return "mail send";

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sendResetToken(String email, String token, String username) {
        String subject = "Yêu cầu đặt lại mật khẩu";
        String urlReset = "http://localhost:5173/reset-password/" + token;
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("resetURL", urlReset);

        String body = templateEngine.process("forgot-password", context);

        String[] cc = new String[1];
        cc[0] = email;

        try {
            return sendMail(email, cc, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
            return "Send mail failure !!!";
        }
    }

    @Override
    public String blockUser(String email, String username, String message) {
        return "";
    }

    @Override
    public String deleteUser(String email, String username) {
        return "";
    }

    @Override
    public String unBlockUser(String email, String username) {
        return "";
    }

    @Override
    public String welcome(String email, String username, String token) {
        String subject = "Thông báo đăng ký tài khoản thành công";

        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("verificationCode", token);

        String body = templateEngine.process("verify-email", context);

        String[] cc = new String[1];
        cc[0] = email;

        try {
            return sendMail(email, cc, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
            return "Send mail failure !!!";
        }
    }

    @Override
    public String resetTokenSuccess(String email, String username) {
        String subject = "Thông báo đặt lại mật khẩu thành công";

        Context context = new Context();
        context.setVariable("username", username);

        String body = templateEngine.process("reset-password", context);

        String[] cc = new String[1];
        cc[0] = email;

        try {
            return sendMail(email, cc, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
            return "Send mail failure !!!";
        }

    }

    @Override
    public String verifiedUser(String email, String username) {
        String subject = "Thông báo đăng ký tài khoản thành công";

        Context context = new Context();
        context.setVariable("username", username);

        String body = templateEngine.process("verified-email", context);

        String[] cc = new String[1];
        cc[0] = email;

        try {
            return sendMail(email, cc, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
            return "Send mail failure !!!";
        }
    }
}
