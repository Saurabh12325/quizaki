package Quiz.QuizWebApplication.Repository;

import Quiz.QuizWebApplication.Entity.AdminEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<AdminEntity, String> {
    Optional<AdminEntity> findByEmailId(String emailId);
}
