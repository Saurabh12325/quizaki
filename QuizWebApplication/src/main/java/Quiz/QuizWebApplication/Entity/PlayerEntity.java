package Quiz.QuizWebApplication.Entity;

import Quiz.QuizWebApplication.Service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("player")
public class PlayerEntity {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registerPlayer")
    public ResponseEntity<AdminEntity> registerAdmin(@RequestBody AdminEntity adminEntity) {
        adminEntity.setEmail(adminEntity.getEmail());
        adminEntity.setPassword(passwordEncoder.encode(adminEntity.getPassword()));
        return playerService.save(adminEntity);
    }
}
