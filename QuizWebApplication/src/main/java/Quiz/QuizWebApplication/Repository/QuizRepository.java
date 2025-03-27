package Quiz.QuizWebApplication.Repository;

import Quiz.QuizWebApplication.DTO.AdminRequestDTO;
import Quiz.QuizWebApplication.DTO.Leaderboard.savePlayerDataDTO;
import Quiz.QuizWebApplication.Entity.PlayerEntity;
import Quiz.QuizWebApplication.Entity.QuizEntity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuizRepository extends MongoRepository<QuizEntity,String> {


    List<AdminRequestDTO> findByEmail(String email);


//    List<PlayerEntity> findAll(String quizId);
}
