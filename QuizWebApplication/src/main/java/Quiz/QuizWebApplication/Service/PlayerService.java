package Quiz.QuizWebApplication.Service;

import Quiz.QuizWebApplication.DTO.CaptchaResponseDTO;

import Quiz.QuizWebApplication.Entity.PlayerEntity;
import Quiz.QuizWebApplication.JWTAuthorisation.JWTService;
import Quiz.QuizWebApplication.Repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
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
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailService emailService;
     @Autowired
      private  RestTemplate restTemplate;

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String RECAPTCHA_SECRET_KEY = "6LfGgaYqAAAAANGBPGKu9_esM12SCBHx8GaiXJq3";




    public ResponseEntity<?> registerPlayer(PlayerEntity playerEntity, String recaptchaToken) {
        // Verify reCAPTCHA
        if (!verifyRecaptcha(recaptchaToken)) {
            return ResponseEntity.badRequest().body("Invalid reCAPTCHA verification.");
        }

        // Check if email already exists
        Optional<PlayerEntity> existingPlayer = playerRepository.findByEmail(playerEntity.getEmail());
        if (existingPlayer.isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }


        String otp = otpService.generateOtp();

        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1); // OTP expires in 1 minute
        playerEntity.setOtp(otp);
        playerEntity.setOtpExpirationTime(expirationTime);
        playerEntity.setVerified(false);
        playerEntity.setPlayerName(playerEntity.getPlayerName());
        playerEntity.setEmail(playerEntity.getEmail());
        playerEntity.setPassword(passwordEncoder.encode(playerEntity.getPassword()));
        playerEntity.setCaptchaResponse(recaptchaToken);
        playerRepository.save(playerEntity);

        // Send OTP to email
        emailService.sendEmail(playerEntity.getEmail(), otp);

        return ResponseEntity.ok("OTP sent to your email!");
    }

    public  ResponseEntity<?> verifyOtp(String email, String otp) {
        Optional<PlayerEntity> playerOtp = playerRepository.findByEmail(email);
        if (playerOtp.isEmpty()) {
            return ResponseEntity.badRequest().body("Player not found.");
        }

        PlayerEntity player = playerOtp.get();

        if (player.getOtpExpirationTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("OTP has expired.");
        }

        if (player.getOtp().equals(otp)) {
            player.setVerified(true);
            playerRepository.save(player);

            String accessToken = jwtService.generateAccessToken(email);
            String refreshToken = jwtService.generateRefreshToken(email);

            return ResponseEntity.ok(Map.of(
                    "message", "Player verified successfully!",
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            ));
        }

        return ResponseEntity.badRequest().body("Invalid OTP.");
    }

    private boolean verifyRecaptcha(String recaptchaToken) {
        CaptchaResponseDTO captchaResponse = restTemplate.postForObject(
                RECAPTCHA_VERIFY_URL,
                Map.of("secret", RECAPTCHA_SECRET_KEY, "response", recaptchaToken),
                CaptchaResponseDTO.class
        );

        return captchaResponse != null && captchaResponse.isSuccess();
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

    public ResponseEntity<?> resendOtp(PlayerEntity playerEntity) {
        Optional<PlayerEntity> existingPlayer = playerRepository.findByEmail(playerEntity.getEmail());

        if (existingPlayer.isEmpty()) {
            return ResponseEntity.badRequest().body("Admin not found!");
        }

        PlayerEntity player = existingPlayer.get();

        if (player.getOtpExpirationTime() != null && player.getOtpExpirationTime().isAfter(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("You can only request a new OTP after the current one expires.");
        }
        String otp = otpService.generateOtp();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1); // OTP expires in 1 minute
        playerEntity.setOtp(otp);
        playerEntity.setOtpExpirationTime(expirationTime);
        playerEntity.setVerified(false);
        playerEntity.setPlayerName(playerEntity.getPlayerName());
        playerEntity.setEmail(playerEntity.getEmail());
        playerEntity.setPassword(passwordEncoder.encode(playerEntity.getPassword()));
        playerRepository.save(playerEntity);
        emailService.sendEmail(playerEntity.getEmail(), otp);



        return ResponseEntity.ok("A new OTP has been sent to your email.");
//        Optional<PlayerEntity> existingPlayer = playerRepository.findByEmail(playerEntity.getEmail());
//        if (existingPlayer.isPresent()) {
//            return ResponseEntity.badRequest().body("Email already exists");
//        }
//        String otp = otpService.generateOtp();
//        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1); // OTP expires in 1 minute
//        playerEntity.setOtp(otp);
//        playerEntity.setOtpExpirationTime(expirationTime);
//        playerEntity.setVerified(false);
//        playerEntity.setPlayerName(playerEntity.getPlayerName());
//        playerEntity.setEmail(playerEntity.getEmail());
//        playerEntity.setPassword(passwordEncoder.encode(playerEntity.getPassword()));
//        playerRepository.save(playerEntity);
//
//        emailService.sendEmail(playerEntity.getEmail(), otp);
//        return ResponseEntity.ok("OTP Send to your Email !");
    }


}

