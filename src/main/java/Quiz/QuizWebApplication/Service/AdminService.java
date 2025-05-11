package Quiz.QuizWebApplication.Service;

import Quiz.QuizWebApplication.DTO.AdminRequestDTO;
import Quiz.QuizWebApplication.Entity.AdminEntity;
import Quiz.QuizWebApplication.Entity.QuizEntity;
import Quiz.QuizWebApplication.JWTAuthorisation.JWTService;
import Quiz.QuizWebApplication.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private QuizService quizService;

    public ResponseEntity<?> registerAdmin(AdminEntity adminEntity) {
        String allowedAdminEmail = "saurabhsri.mau@gmail.com";

        if (!adminEntity.getEmail().equals(allowedAdminEmail)) {
            return ResponseEntity.badRequest().body("Only the predefined email is allowed.");
        }

        Optional<AdminEntity> existingAdmin = adminRepository.findByEmail(adminEntity.getEmail());
        if (existingAdmin.isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }

        String otp = otpService.generateOtp();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1); // OTP expires in 1 minute

        adminEntity.setOtp(otp);
        adminEntity.setOtpExpirationTime(expirationTime);
        adminEntity.setVerified(false);

        adminRepository.save(adminEntity);
        emailService.sendEmail(adminEntity.getEmail(), otp);

        return ResponseEntity.ok("OTP Sent To Your Email.");
    }

    public ResponseEntity<?> verifyOtp(String email, String otp) {
        Optional<AdminEntity> adminOtp = adminRepository.findByEmail(email);

        if (adminOtp.isEmpty()) {
            return ResponseEntity.badRequest().body("Admin not found.");
        }

        AdminEntity admin = adminOtp.get();

        if (admin.getOtpExpirationTime().isBefore(LocalDateTime.now())) {

            return ResponseEntity.badRequest().body("OTP has expired.");
        }

        if (admin.getOtp().equals(otp)) {
            admin.setVerified(true);
            adminRepository.save(admin);
            String accessToken = jwtService.generateAccessToken(email);
            String refreshToken = jwtService.generateRefreshToken(email);

            return ResponseEntity.ok().body(Map.of(
                    "message", "Admin verified successfully!",
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            ));
        }

        return ResponseEntity.badRequest().body("Invalid OTP!");
    }


    public ResponseEntity<?> resendOtp(AdminEntity adminEntity) {

        Optional<AdminEntity> existingAdmin = adminRepository.findByEmail(adminEntity.getEmail());

        if (existingAdmin.isEmpty()) {
            return ResponseEntity.badRequest().body("Admin not found!");
        }

        AdminEntity admin = existingAdmin.get();

        if (admin.getOtpExpirationTime() != null && admin.getOtpExpirationTime().isAfter(LocalDateTime.now().minusSeconds(30))) {
            return ResponseEntity.badRequest().body("You can only request a new OTP after the current one expires.");
        }

        String newOtp = otpService.generateOtp();
        LocalDateTime newExpirationTime = LocalDateTime.now().plusMinutes(1); // New OTP valid for 1 minute

        admin.setOtp(newOtp);
        admin.setOtpExpirationTime(newExpirationTime);
        admin.setVerified(false);
        adminRepository.save(admin);

        emailService.sendEmail(admin.getEmail(), newOtp);

        return ResponseEntity.ok("A new OTP has been sent to your email.");
    }


    public ResponseEntity<?> createQuiz(AdminRequestDTO adminRequestDTO) {
        try {
            QuizEntity createQuiz = quizService.createQuiz(adminRequestDTO);
            return new ResponseEntity<>(createQuiz, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<AdminRequestDTO>> fetchQuizData(String email) {
        try {
            List<AdminRequestDTO> quizEntity = quizService.findByEmail(email);
            if (quizEntity != null) {
                return new ResponseEntity<>(quizEntity, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
