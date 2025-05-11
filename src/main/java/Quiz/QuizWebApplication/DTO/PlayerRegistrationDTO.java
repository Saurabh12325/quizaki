package Quiz.QuizWebApplication.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlayerRegistrationDTO {
    public String getQuizId() {
        return quizId;
    }

    private String quizId;
    public String getUid() {
        return uid;
    }

    private String uid;

    public String getEmail() {
        return email;
    }


    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getOtpExpirationTime() {
        return otpExpirationTime;
    }




    private String playerName;
    private String email;
    private String otp;
    private boolean verified;
    private LocalDateTime otpExpirationTime;

    public String getRecaptchaToken() {
        return recaptchaToken;
    }

    public void setRecaptchaToken(String recaptchaToken) {
        this.recaptchaToken = recaptchaToken;
    }

    private String recaptchaToken;

}
