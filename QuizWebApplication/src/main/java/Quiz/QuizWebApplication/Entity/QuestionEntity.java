package Quiz.QuizWebApplication.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "question")
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
