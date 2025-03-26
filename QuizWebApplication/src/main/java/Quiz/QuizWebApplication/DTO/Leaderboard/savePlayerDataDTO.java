package Quiz.QuizWebApplication.DTO.Leaderboard;

import lombok.Data;

import java.util.List;

@Data
public class savePlayerDataDTO {
    private String playerName;
    private int  correctAnswers;
    private int incorrectAnswers;
    private int streak;
    private int score;
    private int time;
    private List<String> answers;
}
