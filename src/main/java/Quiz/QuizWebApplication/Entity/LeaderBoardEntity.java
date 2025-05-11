package Quiz.QuizWebApplication.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // To avoid unknown property issues
@JsonInclude(JsonInclude.Include.NON_NULL)  // To avoid null fields in response
 // Define the MongoDB collection name
public class LeaderBoardEntity {

    public String getEmail() {
        return email;
    }

    public String getQuizId() {
        return quizId;
    }

    public String getUid() {
        return uid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public int getScore() {
        return score;
    }

    public int getStreak() {
        return streak;
    }

    public int getTime() {
        return time;
    }

    private String email;
    private String quizId;
    private String uid;
    private String playerName;
    private int  correctAnswers;

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public void setTime(int time) {
        this.time = time;
    }

    private int incorrectAnswers;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    private int score;
    private int streak;
    private int time;

}
