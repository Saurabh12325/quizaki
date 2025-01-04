package Quiz.QuizWebApplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    public void sendEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(" Verify Your Email - Cloud Computing Cell QuizApp Registration ");
        message.setText("Welcome to the Cloud Computing Cell!  " +
                " Your One-Time Password (OTP) for completing your registration is:" + otp + " Please enter this OTP on the registration page to verify your email."+" Do not share this code with anyone.");
        mailSender.send(message);
    }


}
