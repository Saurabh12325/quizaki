package Quiz.QuizWebApplication.Entity;

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

    private String quesKey;
    private String questionText;
    private List<String> options;

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    private String correctAnswer;
    private String category;
    private String difficulty;
    public List<String> getOptions() {
        return options;
    }

    public String getQuesKey() {
        return quesKey;
    }

    public String getQuestionText() {
        return questionText;
    }



    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }


}
