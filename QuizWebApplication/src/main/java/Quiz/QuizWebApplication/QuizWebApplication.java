package Quiz.QuizWebApplication;
import Quiz.QuizWebApplication.Repository.AdminRepository;
import Quiz.QuizWebApplication.Repository.PlayerRepository;
import Quiz.QuizWebApplication.Repository.QuestionRepository;
import Quiz.QuizWebApplication.Repository.QuizRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {QuizRepository.class, AdminRepository.class, PlayerRepository.class, QuestionRepository.class})
@EnableScheduling
public class QuizWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuizWebApplication.class, args);
	}
}
