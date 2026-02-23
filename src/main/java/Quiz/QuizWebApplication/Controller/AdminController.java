
package Quiz.QuizWebApplication.Controller;

import Quiz.QuizWebApplication.Config.QuizWebSocketHandler;
import Quiz.QuizWebApplication.DTO.AdminRequestDTO;
import Quiz.QuizWebApplication.DTO.OtpVerificationRequest;
import Quiz.QuizWebApplication.DTO.ResendOtpEmailDto;
import Quiz.QuizWebApplication.Entity.AdminEntity;
import Quiz.QuizWebApplication.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private QuizWebSocketHandler quizWebSocketHandler;

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminEntity adminEntity) {
        return adminService.registerAdmin(adminEntity);
    }
    @PostMapping("/login/Admin")
    public ResponseEntity<?> loginAdmin(@RequestBody AdminRequestDTO.AdminLoginDTO adminLoginDTO){
        return adminService.loginAdmin(adminLoginDTO);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtpLogin(@RequestBody OtpVerificationRequest request) {
        return adminService.verifyOtp(request.getEmail(), request.getOtp());
    }

    @PostMapping("/resendOtp")
    public ResponseEntity<?> resendOtp(@RequestBody ResendOtpEmailDto resendOtpEmailDto) {
        return adminService.resendOtp(resendOtpEmailDto.getEmail());
    }

    @PostMapping("/CreateQuiz")
    public ResponseEntity<?> createQuiz(@RequestBody AdminRequestDTO adminRequestDTO) {
        return adminService.createQuiz(adminRequestDTO);
    }

    @GetMapping("/fetchQuiz")
    public ResponseEntity<List<AdminRequestDTO>> fetchQuizData(@RequestParam String email) {
        return adminService.fetchQuizData(email);
    }
    @PostMapping("/start")
    public String startQuiz() {
        quizWebSocketHandler.notifyAllClients("Quiz Started");
        return "Quiz started notification sent.";
    }
    @PostMapping("/stop")
    public String stopQuiz() {
        quizWebSocketHandler.notifyAllClients("Quiz Stopped");
        return "Quiz stopped notification sent.";
    }
    @PostMapping("/next-question")
    public String sendNextQuestion(@RequestBody String question) {
        quizWebSocketHandler.sendNextQuestion(question);
        return "Next question sent to all players.";
    }
}
