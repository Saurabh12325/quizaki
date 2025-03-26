package Quiz.QuizWebApplication.Service;

import Quiz.QuizWebApplication.DTO.AdminRequestDTO;
import Quiz.QuizWebApplication.DTO.Leaderboard.savePlayerDataDTO;
import Quiz.QuizWebApplication.Entity.PlayerEntity;
import Quiz.QuizWebApplication.Entity.QuizEntity;
import Quiz.QuizWebApplication.Repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;
    public QuizEntity createQuiz(AdminRequestDTO adminRequestDTO) {
            QuizEntity quiz = new QuizEntity();
            quiz.setQuizId(adminRequestDTO.getQuizId());
            quiz.setQuizTitle(adminRequestDTO.getQuizTitle());
            quiz.setQuestions(adminRequestDTO.getQuestions());
            quiz.setEmail(adminRequestDTO.getEmail());
            quiz.setStatus(adminRequestDTO.getStatus());
            quiz.setEndQuizTime(adminRequestDTO.getEndQuizTime());
            quiz.setStartQuizTime(adminRequestDTO.getStartQuizTime());

            return quizRepository.save(quiz);
    }
    public List<AdminRequestDTO> findByEmail(String email) {
        return quizRepository.findByEmail(email);
    }


    public List<PlayerEntity> findByQuizId(String quizId) {
        return quizRepository.findByQuizId(quizId);
    }
}
