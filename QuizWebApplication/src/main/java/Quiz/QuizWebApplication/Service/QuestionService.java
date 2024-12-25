package Quiz.QuizWebApplication.Service;

import Quiz.QuizWebApplication.DTO.QuestionRequestDTO;
import Quiz.QuizWebApplication.Entity.QuestionEntity;
import Quiz.QuizWebApplication.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class QuestionService {
   @Autowired
    private QuestionRepository questionRepository;

    public ResponseEntity<List<QuestionEntity>> getAllQuestions() {
        try {
            List<QuestionEntity> questions = questionRepository.findAll();
            return new ResponseEntity<>(questions, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<QuestionEntity>> getQuestionByCategory(String category) {
        try {
            List<QuestionEntity> questions = questionRepository.findByCategory(category);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<List<QuestionEntity>> getQuestionByDifficulty(String difficulty) {
        try {
            List<QuestionEntity> questions = questionRepository.findByDifficulty(difficulty);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public QuestionEntity createQuestion(QuestionEntity question) {
        QuestionRequestDTO questionsRequestDTO = new QuestionRequestDTO();
        questionsRequestDTO.setDifficulty(question.getDifficulty());
        questionsRequestDTO.setQuestionText(question.getQuestionText());
        questionsRequestDTO.setCategory(question.getCategory());
        questionsRequestDTO.setOptions(question.getOptions());
        questionsRequestDTO.setQuesKey(question.getQuesKey());
       return questionRepository.save(question);
    }
}
