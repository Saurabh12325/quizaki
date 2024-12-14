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
    private String email;
    private String quizTitle;
    private boolean status;
    private List<String> players;
    private List<QuestionEntity> questions;


}
