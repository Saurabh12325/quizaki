package Quiz.QuizWebApplication.Repository;
import Quiz.QuizWebApplication.Entity.AdminEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface AdminRepository extends MongoRepository<AdminEntity, String> {

    Optional<AdminEntity> findByEmail(String email);

//    List<AdminEntity> findByOtpExpirationTimeBefore(LocalDateTime currentTime);

    void deleteAllByOtpExpirationTimeBefore(LocalDateTime time);

//    List<AdminEntity> findByOtpExpirationTimeBefore(LocalDateTime currentTime);

//    void deleteAllByExpirationTimeBefore(LocalDateTime now);
}

