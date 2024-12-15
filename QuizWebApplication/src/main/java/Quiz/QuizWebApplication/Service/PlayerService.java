package Quiz.QuizWebApplication.Service;

import Quiz.QuizWebApplication.DTO.PlayerLoginDTO;
import Quiz.QuizWebApplication.Entity.PlayerEntity;
import Quiz.QuizWebApplication.Repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {
  @Autowired
  private PlayerRepository playerRepository;

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

