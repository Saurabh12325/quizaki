package Quiz.QuizWebApplication.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "quizQues")
public class QuestionEntity {
    @Id
    private String quesKey;
    private String questionText;
   @ElementCollection
    private List<String> options;
    private String correctAnswer;
    private String category;
    private String difficulty;
}
