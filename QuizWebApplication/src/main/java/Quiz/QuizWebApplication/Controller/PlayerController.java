package Quiz.QuizWebApplication.Controller;
import Quiz.QuizWebApplication.DTO.PlayerLoginDTO;
import Quiz.QuizWebApplication.Entity.PlayerEntity;
import Quiz.QuizWebApplication.JWTAuthorisation.JWTService;
import Quiz.QuizWebApplication.Repository.PlayerRepository;
import Quiz.QuizWebApplication.Service.EmailService;
import Quiz.QuizWebApplication.Service.OtpService;
import Quiz.QuizWebApplication.Service.PlayerService;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;
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


    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

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
            return ResponseEntity.badRequest().body("Player not found ");
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
    @DeleteMapping("/LogOut")
    public ResponseEntity<?> DeleteByEmail(@RequestParam String email) {
       return playerService.DeleteByEmail(email);
     }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody PlayerLoginDTO playerLoginDTO) {
        // Step 1: Validate reCAPTCHA
        RestTemplate restTemplate = new RestTemplate();
        String secretKey = "6LeYR5wqAAAAAJIaem88nWR7LvAWI6Yc8yEbLICm";
        Map<String, String> requestBody = Map.of(
                "secret", secretKey,
                "response", playerLoginDTO.getCaptchaResponse()
        );
        Map<String, Object> response = restTemplate.postForObject(VERIFY_URL, requestBody, Map.class);

        if (response == null || !Boolean.TRUE.equals(response.get("success"))) {
            return ResponseEntity.badRequest().body("Invalid CAPTCHA!");
        }

        // Step 2: Authenticate User
        Optional<PlayerEntity> playerOptional = playerRepository.findByEmail(playerLoginDTO.getEmail());
        if (playerOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid email or password!");
        }

        PlayerEntity player = playerOptional.get();
        if (!passwordEncoder.matches(playerLoginDTO.getPassword(), player.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password!");
        }

        if (!player.isVerified()) {
            return ResponseEntity.status(401).body("Email not verified!");
        }

        // Step 3: Generate JWT Tokens
        String accessToken = jwtService.generateAccessToken(playerLoginDTO.getEmail());
        String refreshToken = jwtService.generateRefreshToken(playerLoginDTO.getEmail());

        return ResponseEntity.ok(Map.of(
                "message", "Login successful!",
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }
}




