package com.company.quizapp.controller;

import com.company.quizapp.models.QuestionWrapper;
import com.company.quizapp.models.ResponseObject;
import com.company.quizapp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

        @Autowired
        private QuizService quizService;

        @PostMapping("/create")
        public ResponseEntity<String> createQuiz(@RequestParam String category, @RequestParam int numQ, @RequestParam String title){
            return quizService.createQuiz(category,numQ,title);

        }

        @GetMapping("/{id}")
        public ResponseEntity<List<QuestionWrapper>> findQuizById(@PathVariable("id") int id){
                return quizService.findQuizById(id);
        }

        @PostMapping("submit/{id}")
        public ResponseEntity<Integer> calculateResult(@PathVariable int id, @RequestBody List<ResponseObject> responses){
                return quizService.calculateResult(id,responses);
        }
}
