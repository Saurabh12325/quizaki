package Quiz.QuizWebApplication.Repository;

import Quiz.QuizWebApplication.Entity.PlayerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends MongoRepository<PlayerEntity,String> {
    Optional<PlayerEntity> findByEmail(String playerLoginDTO);
}
