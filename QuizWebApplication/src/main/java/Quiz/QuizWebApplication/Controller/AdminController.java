package Quiz.QuizWebApplication.Controller;
import Quiz.QuizWebApplication.DTO.AdminRequestDTO;
import Quiz.QuizWebApplication.Entity.AdminEntity;
import Quiz.QuizWebApplication.Entity.QuizEntity;
import Quiz.QuizWebApplication.JWTAuthorisation.JWTService;
import Quiz.QuizWebApplication.Repository.AdminRepository;
import Quiz.QuizWebApplication.Service.EmailService;
import Quiz.QuizWebApplication.Service.OtpService;
import Quiz.QuizWebApplication.Service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")

public class AdminController {
      @Autowired
      private  EmailService emailService;
      @Autowired
      private AdminRepository adminRepository;
      @Autowired
     private OtpService otpService;
      @Autowired
    private JWTService jwtService;
      @Autowired
    private QuizService quizService;

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminEntity adminEntity) {

    String allowedAdminEmail = "saurabhsri.mau@gmail.com";

    if (!adminEntity.getEmail().equals(allowedAdminEmail)) {
        return ResponseEntity.badRequest().body("Only the predefined email ia allowed");
    }
    Optional<AdminEntity> existingAdmin = adminRepository.findByEmail(adminEntity.getEmail());
    if (existingAdmin.isPresent()) {
        return ResponseEntity.badRequest().body("Email already exists");
    }
    String otp = otpService.generateOtp();
    adminEntity.setOtp(otp);
    adminEntity.setVerified(false);

    adminRepository.save(adminEntity);
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
            String refreshToken = jwtService.generateRefreshToken(email);

            return ResponseEntity.ok().body(Map.of(
                    "message", "Admin verified successfully!",
                    "accessToken", accessToken,
                    "refreshToken",refreshToken));
        }
        return ResponseEntity.badRequest().body("Invalid OTP!");
    }
    @PostMapping("/CreateQuiz")
    public ResponseEntity<?> createQuiz(@RequestBody AdminRequestDTO  adminRequestDTO) {
        try{
            QuizEntity createQuiz  =  quizService.createQuiz(adminRequestDTO);
            return  new ResponseEntity<>(createQuiz, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/fetchQuiz")
    public ResponseEntity<List<QuizEntity>> fetchQuizData(@RequestParam String email) {
        try {
            List<QuizEntity> quizEntity = quizService.findByEmail(email);
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

