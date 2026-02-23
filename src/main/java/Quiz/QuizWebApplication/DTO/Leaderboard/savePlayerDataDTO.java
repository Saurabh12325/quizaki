package Quiz.QuizWebApplication.DTO.Leaderboard;

import lombok.Data;

import java.util.List;

@Data
public class savePlayerDataDTO {


    private String email;
    private String quizId;
    private String uid;
    private String playerName;
    private int  correctAnswers;
    private int incorrectAnswers;
    private int streak;
    private int score;
    private int time;
    private List<String> answers;


////
//public String getUid() {
//    return uid;
//}
//
//    public String getQuizId() {
//        return quizId;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//    public int  getCorrectAnswers() {
//        return correctAnswers;
//    }
//
//
//
//    public int  getIncorrectAnswers() {
//        return incorrectAnswers;
//    }
//
//
//
//    public int  getStreak() {
//        return streak;
//    }
//
//
//    public int getScore() {
//        return score;
//    }
//
//
//    public int getTime() {
//        return time;
//    }
//
//
//
//    public List<String> getAnswers() {
//        return answers;
//    }
//
//    public void setAnswers(List<String> answers) {
//        this.answers = answers;
//    }




}
