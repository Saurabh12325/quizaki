package Quiz.QuizWebApplication.Entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "admin")
public class AdminEntity {
  private String email;
  private String password;
  private String otp;
  private boolean Verified;

}
