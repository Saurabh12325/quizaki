package Quiz.QuizWebApplication.Entity;

import lombok.Data;
;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "player")
public class PlayerEntity {
    private String id;
   private String PlayerName;
   private String email;
    private String Password;
    private String otp;
    private String captchaResponse;

    public boolean isVerified() {
        return Verified;
    }

    private boolean Verified;
    private int  correctAnswers;
    private int incorrectAnswers;
    private int streak;
    private int score;
    private int time;
    private String quizId;
    private List<String> answers;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


    public String getPlayerName() {
        return PlayerName;
    }

    public void setPlayerName(String playerName) {
        PlayerName = playerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }



    public void setVerified(boolean verified) {
        Verified = verified;
    }



}

