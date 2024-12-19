package Quiz.QuizWebApplication.Service;

import Quiz.QuizWebApplication.DTO.PlayerLoginDTO;
import Quiz.QuizWebApplication.Entity.PlayerEntity;
import Quiz.QuizWebApplication.JWTAuthorisation.JWTService;
import Quiz.QuizWebApplication.Repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class PlayerService {
  @Autowired
  private PlayerRepository playerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public ResponseEntity<?> login(PlayerLoginDTO playerLoginDTO) {
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


    public ResponseEntity<?> DeleteByEmail(String email) {
        try {
            Optional<PlayerEntity> existingPlayer = playerRepository.findByEmail(email);
            if(existingPlayer.isPresent()) {
                playerRepository.delete(existingPlayer.get());
                return new ResponseEntity<>("Logout Succesfully", HttpStatus.OK);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
       return new ResponseEntity<>("Logout Failed", HttpStatus.NOT_FOUND);
    }

}

