package Quiz.QuizWebApplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    public void sendEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(" Verify Your Email - Cloud Computing Cell QuizApp Registration ");
        message.setText("✨ **Welcome to the Cloud Computing Cell!** ✨\" \n" +
                        "     \"We're excited to have you onboard. To complete your registration, please use the One-Time Password (OTP) provided below:\" \n" +
                        "    \" Your OTP:- " + otp +
                        "      \n For security reasons, do not share this OTP with anyone.*\" \n" +
                        "     \"✅ Enter this code on the registration page to verify your email and unlock access to the QuizApp.\" +\n" +
                        "       \"If you did not request this, please ignore this email.\" +\n" +
                        "        \"Best Regards,\" \n" + "\nCloud Computing Cell Team");
        message.setSentDate(new Date());
        mailSender.send(message);
    }


}
