package Quiz.QuizWebApplication.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "quiz")
public class QuizEntity {

    private String quizId;
    private String email;
    private String quizTitle;
    private String status;
    private String startQuizTime;

    public void setStartQuizTime(String startQuizTime) {
        this.startQuizTime = startQuizTime;
    }

    public void setEndQuizTime(String endQuizTime) {
        this.endQuizTime = endQuizTime;
    }

    private String endQuizTime;
    private List<String> players;
    private List<QuestionEntity> questions;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public void setQuestions(List<QuestionEntity> questions) {
        this.questions = questions;
    }


}
