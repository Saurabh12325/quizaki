package Quiz.QuizWebApplication.Repository;

import Quiz.QuizWebApplication.Entity.QuestionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public  interface QuestionRepository extends MongoRepository<QuestionEntity, String> {
    List<QuestionEntity> findByCategory(String category);
    List<QuestionEntity> findByDifficulty(String difficulty);
}
