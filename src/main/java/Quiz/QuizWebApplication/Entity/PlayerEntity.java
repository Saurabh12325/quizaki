package Quiz.QuizWebApplication.Entity;

import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "player")
public class PlayerEntity {


    private String id;
    private String playerName;
    private String email;
    private String otp;
    private  String uid;
    private boolean Verified;
    private int  correctAnswers;
    private int incorrectAnswers;
    private  int  streak;
    private int score;
    private int time;
    private String quizId;
    private List<String> answers;
    private String recaptchaToken;
    private LocalDateTime otpExpirationTime;
//
//
//    public void setUid(String uid) {
//        this.uid = uid;
//    }
//
//    public String getUid() {
//        return uid;
//    }
//
//
//
//    public void setPlayerName(String playerName) {
//        this.playerName = playerName;
//    }
//
//    public String getPlayerName() {
//        return playerName;
//    }
//
//
//    public boolean isVerified() {
//        return Verified;
//    }
//
//
//
//    public void setTime(int time) {
//        this.time = time;
//    }
//
//    public void setScore(int score) {
//        this.score = score;
//    }
//
//    public void setStreak(int streak) {
//        this.streak = streak;
//    }
//
//    public void setIncorrectAnswers(int incorrectAnswers) {
//        this.incorrectAnswers = incorrectAnswers;
//    }
//
//    public void setCorrectAnswers(int correctAnswers) {
//        this.correctAnswers = correctAnswers;
//    }
//
//    public int getCorrectAnswers() {
//        return correctAnswers;
//    }
//
//    public int getIncorrectAnswers() {
//        return incorrectAnswers;
//    }
//
//    public int getStreak() {
//        return streak;
//    }
//
//    public int getScore() {
//        return score;
//    }
//
//    public int getTime() {
//        return time;
//    }
//
//
//
//    public String getQuizId() {
//        return quizId;
//    }
//
//    public void setQuizId(String quizId) {
//        this.quizId = quizId;
//    }
//
//
//
//
//    public void setOtpExpirationTime(LocalDateTime otpExpirationTime) {
//        this.otpExpirationTime = otpExpirationTime;
//    }
//
//    public LocalDateTime getOtpExpirationTime() {
//        return otpExpirationTime;
//    }
//
//
//
//    public String getRecaptchaToken() {
//        return recaptchaToken;
//    }
//
//    public void setRecaptchaToken(String recaptchaToken) {
//        this.recaptchaToken = recaptchaToken;
//    }
//
//
//
////    public String getPassword() {
////        return Password;
////    }
////
////    public void setPassword(String password) {
////        Password = password;
////    }
//
//
//
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getOtp() {
//        return otp;
//    }
//
//    public void setOtp(String otp) {
//        this.otp = otp;
//    }
//
//
//
//    public void setVerified(boolean verified) {
//        Verified = verified;
//    }



}

