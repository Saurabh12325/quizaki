package Quiz.QuizWebApplication.DTO;

import Quiz.QuizWebApplication.Entity.QuestionEntity;
import lombok.Data;


import java.util.List;
@Data

public class AdminRequestDTO {
    private String quizId;
    private String quizTitle;
    private String email;
    private String status;
    private List<QuestionEntity> questions;

    public String getStatus() {
        return status;
    }
    public String getQuizTitle() {
        return quizTitle;
    }

    public String getQuizId() {
        return quizId;
    }

    public String getEmail() {
        return email;
    }

    public List<QuestionEntity> getQuestions() {
        return questions;
    }


}
