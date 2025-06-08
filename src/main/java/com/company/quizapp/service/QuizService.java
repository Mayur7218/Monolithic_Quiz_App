package com.company.quizapp.service;

import com.company.quizapp.models.Question;
import com.company.quizapp.models.QuestionWrapper;
import com.company.quizapp.models.Quiz;
import com.company.quizapp.models.ResponseObject;
import com.company.quizapp.repository.QuestionDao;
import com.company.quizapp.repository.QuizDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class QuizService {

    @Autowired
    private QuizDao dao;

    @Autowired
    private QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title ) {
        try {
            List<Question> questions=questionDao.findRandomByCategory(category,numQ);

            Quiz quiz=new Quiz();
            quiz.setTitle(title);
            quiz.setQuestions(questions);

            dao.save(quiz);
            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<QuestionWrapper>> findQuizById(int id) {
        List<QuestionWrapper> questionForUsers=new ArrayList<>();
       try {
           Optional<Quiz> quiz=dao.findById(id);
           List<Question> questionsFromDB =quiz.get().getQuestions();

           for (Question q:questionsFromDB){
               QuestionWrapper qw=new QuestionWrapper(q.getId(),q.getQuestionTitle(),q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4());
               questionForUsers.add(qw);
           }
           Comparator<QuestionWrapper> com=new Comparator<QuestionWrapper>() {
               @Override
               public int compare(QuestionWrapper o1, QuestionWrapper o2) {
                   if(o1.getId()>o2.getId()){
                       return 1;
                   }else{
                       return -1;
                   }
               }
           };
           questionForUsers.sort(com);
           return new ResponseEntity<>(questionForUsers,HttpStatus.OK);
       }catch (Exception e){
           e.printStackTrace();
       }
        return new ResponseEntity<>(questionForUsers,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Integer> calculateResult(int id, List<ResponseObject> responses) {
        int rightAnswers=0;
        try {
            Quiz quiz=dao.findById(id).get();
            List<Question> questionList=quiz.getQuestions();
            for(ResponseObject response:responses){
                for(int i=0;i<questionList.size();i++){
                    if(response.getResponse().equals(questionList.get(i).getRightAnswer())){
                        rightAnswers++;
                    }
                }
            }
            return new ResponseEntity<>(rightAnswers,HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(rightAnswers,HttpStatus.BAD_REQUEST);
    }
}
