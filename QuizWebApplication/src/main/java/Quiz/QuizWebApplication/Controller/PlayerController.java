package Quiz.QuizWebApplication.Controller;


import Quiz.QuizWebApplication.Entity.PlayerEntity;
import Quiz.QuizWebApplication.JWTAuthorisation.JWTService;
import Quiz.QuizWebApplication.Repository.PlayerRepository;
import Quiz.QuizWebApplication.Service.EmailService;
import Quiz.QuizWebApplication.Service.OtpService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/player")
public class PlayerController {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailService emailService;
@Autowired
private PasswordEncoder passwordEncoder;
@Autowired
private JWTService jwtService;
@PostMapping("/registerPlayer")
    public ResponseEntity<?> registerPlayer(@RequestBody PlayerEntity playerEntity) {
    Optional<PlayerEntity> existingPlayer = playerRepository.findByEmail(playerEntity.getEmail());
    if (existingPlayer.isPresent()) {
        return ResponseEntity.badRequest().body("Email already exists");
    }
    String otp = otpService.generateOtp();
    playerEntity.setOtp(otp);
    playerEntity.setVerified(false);
    playerEntity.setPlayerName(playerEntity.getPlayerName());
    playerEntity.setEmail(playerEntity.getEmail());
    playerEntity.setPassword(passwordEncoder.encode(playerEntity.getPassword()));
    playerRepository.save(playerEntity);

    emailService.sendEmail(playerEntity.getEmail(), otp);
    return ResponseEntity.ok("OTP Send to your Email !");
}
    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        Optional<PlayerEntity> playerOtp = playerRepository.findByEmail(email);
        if (playerOtp.isEmpty()) {
            return ResponseEntity.badRequest().body("Admin not found ");
        }
        PlayerEntity player = playerOtp.get();
        if (player.getOtp().equals(otp)) {
            player.setVerified(true);
            playerRepository.save(player);
            String accessToken = jwtService.generateAccessToken(email);
            String refreshToken = jwtService.generateRefreshToken(email);

            return ResponseEntity.ok().body(Map.of(
                    "message", "Player verified successfully!",
                    "accessToken", accessToken,
                    "refreshToken", refreshToken));

        }
        return ResponseEntity.badRequest().body("Invalid OTP!");

    }
}

