package Quiz.QuizWebApplication.DTO;

import lombok.Data;

@Data
public class AdminLoginDTO {
    private String email;
    private String password;
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//d
}
