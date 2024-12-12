package Quiz.QuizWebApplication.Entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "quiz")
public class QuizEntity {
    @Id
    private String quizId;
    private String emailId;
    private String quizTitle; // Title of the quiz
    private boolean status;
    private List<String> players;
    private List<QuestionEntity> questions;


}
