package Quiz.QuizWebApplication.Service;

import Quiz.QuizWebApplication.DTO.AdminRequestDTO;
import Quiz.QuizWebApplication.DTO.CaptchaResponseDTO;

import Quiz.QuizWebApplication.DTO.Leaderboard.savePlayerDataDTO;
import Quiz.QuizWebApplication.DTO.PlayerRegistrationDTO;
import Quiz.QuizWebApplication.Entity.LeaderBoardEntity;
import Quiz.QuizWebApplication.Entity.PlayerEntity;
import Quiz.QuizWebApplication.JWTAuthorisation.JWTService;
import Quiz.QuizWebApplication.Repository.LeaderBoardRepository;
import Quiz.QuizWebApplication.Repository.PlayerRepository;
import Quiz.QuizWebApplication.Repository.QuizRepository;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private QuizService quizService;
    @Autowired
    private LeaderBoardRepository leaderBoardRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private EmailService emailService;


    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

//    private static  final String RECAPTCHA_SECRET_KEY = "6LcQ9poqAAAAAB2VstnYoQ6fcyInEDuapEAw0viU";
    private static  final String RECAPTCHA_SECRET_KEY = "6LdObnUsAAAAALaGPfjWu5gsYu0g0OZTlzcO2-UM";






    public ResponseEntity<?> registerPlayer(PlayerRegistrationDTO playerRegistrationDTO) {

        String recaptchaToken = playerRegistrationDTO.getRecaptchaToken();
        if (recaptchaToken == null || recaptchaToken.isEmpty()) {
            return ResponseEntity.badRequest().body("reCAPTCHA token is missing.");
        }
        // Verify reCAPTCHA
        if (!verifyRecaptcha(recaptchaToken)) {
            return ResponseEntity.badRequest().body("Invalid reCAPTCHA verification.");
      }

        // Check if email already exists
        Optional<PlayerEntity> existingPlayer = playerRepository.findByEmail(playerRegistrationDTO.getEmail());
        if (existingPlayer.isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }

        // Generate OTP
        String otp = otpService.generateOtp();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(20); // OTP expires in 20 minute

        // Map DTO to Entity
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setQuizId(playerRegistrationDTO.getQuizId());
        playerEntity.setUid(playerRegistrationDTO.getUid());
        playerEntity.setPlayerName(playerRegistrationDTO.getPlayerName());
        playerEntity.setEmail(playerRegistrationDTO.getEmail());
        playerEntity.setOtp(otp);
        playerEntity.setOtpExpirationTime(expirationTime);
        playerEntity.setCorrectAnswers(0); // Default values for new player
        playerEntity.setIncorrectAnswers(0);
        playerEntity.setScore(0);
        playerEntity.setTime(0);
        playerEntity.setStreak(0);


        playerEntity.setVerified(false);
      //  playerEntity.setPassword(passwordEncoder.encode(playerRegistrationDTO.getPassword())); // Encode password

        // Save to repository
        playerRepository.save(playerEntity);

        // Send OTP to email
        emailService.sendEmail(playerEntity.getEmail(), otp);


        return ResponseEntity.ok("OTP sent to your email!");
    }

    public  ResponseEntity<?> verifyOtp(String email, String otp) {
        Optional<PlayerEntity> playerEntity = playerRepository.findByEmail(email);
        if (playerEntity.isEmpty()) {
            return ResponseEntity.badRequest().body("Player not found.");
        }

        PlayerEntity player = playerEntity.get();

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
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("secret", RECAPTCHA_SECRET_KEY);
        requestBody.add("response", recaptchaToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<CaptchaResponseDTO> response = restTemplate.postForEntity(
                RECAPTCHA_VERIFY_URL,
                request,
                CaptchaResponseDTO.class
        );

        return response.getBody() != null && response.getBody().isSuccess();

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
            return ResponseEntity.badRequest().body("Player not found!");
        }

        PlayerEntity player = existingPlayer.get();

        if (player.getOtpExpirationTime() != null && player.getOtpExpirationTime().isAfter(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("You can only request a new OTP after the current one expires.");
        }
        String otp = otpService.generateOtp();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(20); // OTP expires in 1 minute
        playerEntity.setOtp(otp);
        playerEntity.setOtpExpirationTime(expirationTime);
        playerEntity.setVerified(false);
        playerEntity.setPlayerName(playerEntity.getPlayerName());
        playerEntity.setEmail(playerEntity.getEmail());
//        playerEntity.setPassword(passwordEncoder.encode(playerEntity.getPassword()));
        playerRepository.save(playerEntity);
        emailService.sendEmail(playerEntity.getEmail(), otp);



        return ResponseEntity.ok("A new OTP has been sent to your email.");

    }


    public ResponseEntity<String>saveUserData(savePlayerDataDTO playerData) {
        PlayerEntity existingPlayer = playerRepository.findByUid(playerData.getUid());

      if (existingPlayer == null) {
          return ResponseEntity.badRequest().body("Player not found.");
      }
        existingPlayer.setQuizId(playerData.getQuizId());
        existingPlayer.setCorrectAnswers(playerData.getCorrectAnswers());
        existingPlayer.setIncorrectAnswers(playerData.getIncorrectAnswers());
        existingPlayer.setScore(playerData.getScore());
        existingPlayer.setStreak(playerData.getStreak());
        existingPlayer.setTime(playerData.getTime());


        playerRepository.save(existingPlayer);
        // Save data to leaderboard
        LeaderBoardEntity leaderBoardEntity = new LeaderBoardEntity();
        leaderBoardEntity.setEmail(existingPlayer.getEmail());
        leaderBoardEntity.setQuizId(existingPlayer.getQuizId());
        leaderBoardEntity.setUid(existingPlayer.getUid());
        leaderBoardEntity.setPlayerName(existingPlayer.getPlayerName());
        leaderBoardEntity.setCorrectAnswers(existingPlayer.getCorrectAnswers());
        leaderBoardEntity.setIncorrectAnswers(existingPlayer.getIncorrectAnswers());
        leaderBoardEntity.setScore(existingPlayer.getScore());
        leaderBoardEntity.setStreak(existingPlayer.getStreak());
        leaderBoardEntity.setTime(existingPlayer.getTime());
        leaderBoardRepository.save(leaderBoardEntity);

        return ResponseEntity.ok("Player saved and added to leaderboard.");

        // API to fetch leaderboard data
//

        }
    public ResponseEntity<List<LeaderBoardEntity>> getLeaderboard(String quizId) {
        List<LeaderBoardEntity> leaderboard = leaderBoardRepository.findByQuizId(quizId);
        if (leaderboard.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(leaderboard, HttpStatus.OK);
    }


}


//    public ResponseEntity<List<PlayerEntity>> fetchPlayer(String quizId) {
//
//            try {
//                List<PlayerEntity> playerEntity = quizService.findByQuizId(quizId);
//                if (playerEntity != null) {
//                    return new ResponseEntity<>(playerEntity, HttpStatus.OK);
//                } else {
//                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//                }
//            } catch (Exception e) {
//                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//    }



