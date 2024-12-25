
package Quiz.QuizWebApplication.Controller;

import Quiz.QuizWebApplication.DTO.AdminRequestDTO;
import Quiz.QuizWebApplication.Entity.AdminEntity;
import Quiz.QuizWebApplication.Service.AdminService;
import Quiz.QuizWebApplication.Entity.QuizEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminEntity adminEntity) {
        return adminService.registerAdmin(adminEntity);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtpLogin(@RequestParam String email, @RequestParam String otp) {
        return adminService.verifyOtp(email, otp);
    }

    @PostMapping("/resendOtp")
    public ResponseEntity<?> resendOtp(@RequestBody AdminEntity adminEntity) {
        return adminService.resendOtp(adminEntity);
    }

    @PostMapping("/CreateQuiz")
    public ResponseEntity<?> createQuiz(@RequestBody AdminRequestDTO adminRequestDTO) {
        return adminService.createQuiz(adminRequestDTO);
    }

    @GetMapping("/fetchQuiz")
    public ResponseEntity<List<QuizEntity>> fetchQuizData(@RequestParam String email) {
        return adminService.fetchQuizData(email);
    }
}
