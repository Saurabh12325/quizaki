package Quiz.QuizWebApplication.Repository;

import Quiz.QuizWebApplication.Entity.LeaderBoardEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderBoardRepository extends MongoRepository<LeaderBoardEntity,String> {
    List<LeaderBoardEntity> findByQuizId(String quizId);

}
