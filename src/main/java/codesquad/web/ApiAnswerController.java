package codesquad.web;

import codesquad.UnAuthenticationException;
import codesquad.domain.Answer;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.AnswerDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {
    @Autowired
    private QnaService qnaService;

    @PostMapping("")
    public ResponseEntity<Void> create(@LoginUser User loginUser, @PathVariable long questionId, @Valid @RequestBody AnswerDto answerDto) throws UnAuthenticationException {
        Answer savedAnswer = qnaService.createAnswer(loginUser, questionId, answerDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(savedAnswer.generateApiUrl()));
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public AnswerDto show(@PathVariable long id) {
        Answer answer = qnaService.findAnswerById(id);
        return answer.toAnswerDto();
    }
//
//    @PutMapping("{id}")
//    public void update(@LoginUser User loginUser, @PathVariable long id, @Valid @RequestBody QuestionDto updatedQuestion) {
//        qnaService.update(loginUser, id, updatedQuestion.toQuestion());
//    }
//
//    @DeleteMapping("{id}")
//    public void delete(@LoginUser User loginUser, @PathVariable long id) {
//        qnaService.deleteQuestion(loginUser, id);
//    }
}
