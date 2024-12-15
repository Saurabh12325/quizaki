package Quiz.QuizWebApplication.DTO;

import lombok.Data;

@Data
public class PlayerLoginDTO {
    private String email;
    private String Password;
    private String captchaResponse;
//    private String quizId;
    public String getPassword() {
        return Password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCaptchaResponse() {
        return captchaResponse;
    }






}
