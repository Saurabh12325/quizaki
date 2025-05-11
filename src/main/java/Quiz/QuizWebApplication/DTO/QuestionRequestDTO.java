package Quiz.QuizWebApplication.DTO;


import lombok.Data;

import java.util.List;
@Data
public class QuestionRequestDTO {
    private String quesKey;
    private String questionText;
    private List<String> options;
    private String correctAnswer;
    private String category;
    private String difficulty;

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setQuesKey(String quesKey) {
        this.quesKey = quesKey;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}
