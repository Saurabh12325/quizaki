package Quiz.QuizWebApplication.Controller;
import Quiz.QuizWebApplication.Entity.PlayerEntity;
import Quiz.QuizWebApplication.Service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/registerPlayer")
    public ResponseEntity<?> registerPlayer(@RequestBody PlayerEntity playerEntity, @RequestParam String recaptchaToken) {
        return playerService.registerPlayer(playerEntity, recaptchaToken);
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


}




