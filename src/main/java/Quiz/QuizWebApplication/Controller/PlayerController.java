package Quiz.QuizWebApplication.Controller;
import Quiz.QuizWebApplication.DTO.Leaderboard.savePlayerDataDTO;
import Quiz.QuizWebApplication.DTO.PlayerRegistrationDTO;
import Quiz.QuizWebApplication.Entity.LeaderBoardEntity;
import Quiz.QuizWebApplication.Entity.PlayerEntity;
import Quiz.QuizWebApplication.Repository.PlayerRepository;
import Quiz.QuizWebApplication.Repository.QuizRepository;
import Quiz.QuizWebApplication.Service.PlayerService;
import Quiz.QuizWebApplication.Service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private QuizRepository quizRepository;

    @PostMapping("/registerPlayer")
    public ResponseEntity<?> registerPlayer(@RequestBody PlayerRegistrationDTO playerRegistrationDTO) {
        return playerService.registerPlayer(playerRegistrationDTO);
}
    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return playerService.verifyOtp(email,otp);
    }
    @PostMapping("/resendOtp")
    public ResponseEntity<?> resendOtp(@RequestBody PlayerEntity playerEntity) {
        return playerService.resendOtp(playerEntity);
    }
    @DeleteMapping("/LogOut")
    public ResponseEntity<?> DeleteByEmail(@RequestParam String email) {
       return playerService.DeleteByEmail(email);
     }

     @PostMapping("/SavePlayer")
     public ResponseEntity<String> saveUserData(@RequestBody savePlayerDataDTO userDataDTO) {
         return playerService.saveUserData(userDataDTO);
     }

    @GetMapping("/leaderboard/{quizId}")
    public ResponseEntity<List<LeaderBoardEntity>> getLeaderboard(@PathVariable String quizId) {
        return playerService.getLeaderboard(quizId);
    }

}




