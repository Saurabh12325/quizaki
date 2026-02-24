package Quiz.QuizWebApplication.Service;

import Quiz.QuizWebApplication.DTO.AdminRequestDTO;
import Quiz.QuizWebApplication.Entity.AdminEntity;
import Quiz.QuizWebApplication.Entity.QuizEntity;
import Quiz.QuizWebApplication.JWTAuthorisation.JWTService;
import Quiz.QuizWebApplication.Repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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
    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResponseEntity<?> registerAdmin(AdminEntity adminEntity){
        String allowedAdmin = "saurabhsri.mau@gmail.com";
        if(!adminEntity.getEmail().equals(allowedAdmin)){
            return ResponseEntity.badRequest().body("Only the predefined email is allowed.");
        }
        Optional<AdminEntity> existingAdmin = adminRepository.findByEmail(adminEntity.getEmail());
        if(existingAdmin.isEmpty()){
            AdminEntity admin = existingAdmin.orElseGet(AdminEntity::new);

            String otp = otpService.generateOtp();

            admin.setEmail(adminEntity.getEmail());
            admin.setPassword(passwordEncoder.encode(adminEntity.getPassword()));
            admin.setOtp(otp);
            admin.setOtpGeneratedTime(LocalDateTime.now());
            admin.setCreatedAt(LocalDateTime.now());
            adminRepository.save(admin);
            emailService.sendEmail(adminEntity.getEmail(), otp);
            return ResponseEntity.ok("OTP sent to your email " + adminEntity.getEmail());

        } else if (!existingAdmin.get().isVerified()) {
            return ResponseEntity.badRequest().body("Please verify your email with otp");
        }
       return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("User Already Register Please login !");
    }


    public ResponseEntity<?> verifyOtp(String email, String requestOtp){
        Optional<AdminEntity> adminEntity = adminRepository.findByEmail(email);
        if(adminEntity.isEmpty()){
            return ResponseEntity.badRequest().body("Admin not found!");
        }
        AdminEntity admin = adminEntity.get();
        if (admin.isVerified()) {
            throw new RuntimeException("Email already verified.");
        }

        if(!requestOtp.equals(admin.getOtp())){
            return ResponseEntity.badRequest().body("Invalid OTP!");
        }
        if(admin.getOtpGeneratedTime() == null || Duration.between(admin.getOtpGeneratedTime(), LocalDateTime.now()).toMinutes()>1){
            return ResponseEntity.badRequest().body("OTP has expired! Please request a new one ");
        }
        admin.setVerified(true);
        adminRepository.save(admin);
     return ResponseEntity.ok("Admin verified successfully!");
    }

    public ResponseEntity<?> resendOtp(String email){
        Optional<AdminEntity> existingAdmin = adminRepository.findByEmail(email);
        if(existingAdmin.isEmpty()){
            return ResponseEntity.badRequest().body("Admin not registered!");
        }

        AdminEntity admin = existingAdmin.get();

        if(admin.isVerified()){
            return ResponseEntity.badRequest().body("Admin already verified!");
        }

        if(admin.getOtpGeneratedTime() != null
                && Duration.between(admin.getOtpGeneratedTime(), LocalDateTime.now()).toMinutes() < 1) {

            long waitTime = 1 - Duration.between(admin.getOtpGeneratedTime(), LocalDateTime.now()).toMinutes();
            return ResponseEntity.status(HttpStatus.TOO_EARLY)
                    .body("Please wait " + waitTime + " more minute(s) before resending OTP.");
        }

        // Generate new OTP
        String newOtp = otpService.generateOtp();
        admin.setOtp(newOtp);
        admin.setOtpGeneratedTime(LocalDateTime.now());
        adminRepository.save(admin);

        emailService.sendEmail(email, newOtp);

        return ResponseEntity.ok("New OTP sent to your email " + email);
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

    public ResponseEntity<?> loginAdmin(AdminRequestDTO.AdminLoginDTO adminLoginDTO) {

        Optional<AdminEntity> existingAdmin = adminRepository.findByEmail(adminLoginDTO.getEmail());
        if (existingAdmin.isEmpty()) {
            return ResponseEntity.badRequest().body("Admin not found.");
        }
        AdminEntity admin = existingAdmin.get();
        if(admin.isVerified() && (passwordEncoder.matches(adminLoginDTO.getPassword(), admin.getPassword())) ){
            String accessToken = jwtService.generateAccessToken(adminLoginDTO.getEmail());
            String refreshToken = jwtService.generateRefreshToken(adminLoginDTO.getEmail());

            return ResponseEntity.ok().body(Map.of(
                    "message", "Admin Login successfully!",
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            ));
        }
        return ResponseEntity.badRequest().body("Invalid Admin email Password!");

    }
}
//njfn