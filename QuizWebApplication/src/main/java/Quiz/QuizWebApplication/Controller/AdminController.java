package Quiz.QuizWebApplication.Controller;
import Quiz.QuizWebApplication.Entity.AdminEntity;
//import Quiz.QuizWebApplication.JWTAuthorisation.JwtService;
import Quiz.QuizWebApplication.JWTAuthorisation.JWTService;
import Quiz.QuizWebApplication.Repository.AdminRepository;
import Quiz.QuizWebApplication.Service.AdminService;
import Quiz.QuizWebApplication.Service.EmailService;
import Quiz.QuizWebApplication.Service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService  emailService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private OtpService otpService;
    @Autowired
    private JWTService jwtService;


    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminEntity adminEntity) {

    String allowedAdminEmail = "ashishkumarr0856@gmail.com";

    if (!adminEntity.getEmail().equals(allowedAdminEmail)) {
        return ResponseEntity.badRequest().body("Only the predefined email ia allowed");
    }
    Optional<AdminEntity> existingAdmin = adminRepository.findByEmail(adminEntity.getEmail());
    if (existingAdmin.isPresent()) {
        return ResponseEntity.badRequest().body("Email already exists");
    }
    //here save the admin with the otp
    String otp = otpService.generateOtp();
    adminEntity.setOtp(otp);
    adminEntity.setVerified(false);
    adminRepository.save(adminEntity);

    //yha se otp send
    emailService.sendEmail(adminEntity.getEmail(), otp);
    return ResponseEntity.ok("OTP Sent To Your Email");

}


    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtpLogin(@RequestParam String email,@RequestParam String otp) {
        Optional<AdminEntity> adminOtp = adminRepository.findByEmail(email);
        if(adminOtp.isEmpty()) {
            return ResponseEntity.badRequest().body("Admin not found ");
        }
        AdminEntity admin = adminOtp.get();
        if(admin.getOtp().equals(otp)) {
            admin.setVerified(true);
            adminRepository.save(admin);
            String accessToken = jwtService.generateAccessToken(email);

            return ResponseEntity.ok().body(Map.of(
                    "message", "Admin verified successfully!",
                    "accessToken", accessToken
            ));

        }
        return ResponseEntity.badRequest().body("Invalid OTP!");
    }

}
