package Quiz.QuizWebApplication.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CaptchaResponseDTO {


    private String challenge_ts;
    private String hostname;
    @JsonProperty("error-codes")
    private String[] errorCodes;
    @JsonProperty("success")
    private boolean success;
//    public boolean isSuccess() {
//        return success;
//    }
//
//    public void setSuccess(boolean success) {
//        this.success = success;
//    }
}
