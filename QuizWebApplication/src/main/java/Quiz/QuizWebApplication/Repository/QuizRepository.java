package Quiz.QuizWebApplication.Repository;

import Quiz.QuizWebApplication.Entity.QuizEntity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuizRepository extends MongoRepository<QuizEntity,String> {
    List<QuizEntity> findByEmail(String email);
}
