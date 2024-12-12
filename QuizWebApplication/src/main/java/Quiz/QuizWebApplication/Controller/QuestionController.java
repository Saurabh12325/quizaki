package Quiz.QuizWebApplication.Controller;

import Quiz.QuizWebApplication.Entity.QuestionEntity;
import Quiz.QuizWebApplication.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quizzes")
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
    public ResponseEntity<QuestionEntity> addQuestion(@RequestBody QuestionEntity question){
        return questionService.save(question);
    }


}
