package Quiz.QuizWebApplication.Controller;

import Quiz.QuizWebApplication.Entity.QuestionEntity;
import Quiz.QuizWebApplication.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("AllQuestions")
    public ResponseEntity<List<QuestionEntity>> getAllQuestions(){
        return questionService.getAllQuestions();
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<QuestionEntity>> getQustionsByCategory(@PathVariable String category){
        return  questionService.getQuestionByCategory(category);
    }
    @GetMapping("difficulty/{difficulty}")
    public ResponseEntity<List<QuestionEntity>> getQuestionsByDifficulty(@PathVariable String difficulty){
        return questionService.getQuestionByDifficulty(difficulty);
    }
    @PostMapping("/add")
    public ResponseEntity<QuestionEntity> createQuestion(@RequestBody QuestionEntity question){
        try{
            QuestionEntity questions = questionService.createQuestion(question);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
