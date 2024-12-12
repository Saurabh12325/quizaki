package Quiz.QuizWebApplication.Service;

import Quiz.QuizWebApplication.DTO.AdminRequestDTO;
import Quiz.QuizWebApplication.Entity.AdminEntity;
import Quiz.QuizWebApplication.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
@Autowired
private AdminRepository adminRepository;
@Autowired
private JavaMailSender mailSender;

    public  ResponseEntity<String> registerAdmin(AdminEntity admin) {
        Optional<AdminEntity> existingAdmin = adminRepository.findByEmailId(admin.getEmailId());
        if (existingAdmin.isPresent()) {
            return new ResponseEntity<>("Email already Registerd !", HttpStatus.ALREADY_REPORTED);
        }
        // otp generate
        String Otp = generateOTP();
        admin.setOtp(Otp);
        admin.setVerified(false);
        adminRepository.save(admin);

        //Send otp on Email

        SendOtpEmail(admin.getEmailId(),Otp);
        return new ResponseEntity<>("Admin Registered ,Otp sent to Email !", HttpStatus.OK);

    }
}
