package codesquad.web;

import codesquad.domain.Question;
import codesquad.dto.AnswerDto;
import codesquad.dto.QuestionDto;
import codesquad.service.QnaService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {

    @Autowired
    private QnaService qnaService;

    private Question question;

    @Before
    public void setUp() throws Exception {
        long questionId = 1;

        super.setDefaultRequestUrl(String.format("/api/questions/%d/answers", questionId));
        question = qnaService.findQuestionById(questionId);
    }

    @Test
    public void create_있는질문_로그인된상황() throws Exception {
        AnswerDto newAnswer = createAnswerDto(1);
        String location = createResource(newAnswer);

        AnswerDto insertedAnswerDto = getResource(location, template(), AnswerDto.class);
        assertEquals(newAnswer, insertedAnswerDto);
    }
//
//    @Test
//    public void create_not_login() throws Exception {
//        AnswerDto newQuestionDto = createAnswerDto(2);
//        ResponseEntity<String> response = template().postForEntity(super.getDefaultRequestUrl(), newQuestionDto, String.class);
//        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
//    }
//
//    @Test
//    public void show_게스트() {
//        AnswerDto newQuestionDto = createAnswerDto(3);
//        String location = createResource(newQuestionDto);
//
//        QuestionDto insertedQuestionDto = getResource(location, template(), QuestionDto.class);
//        assertEquals(newQuestionDto, insertedQuestionDto);
//    }
//
//    @Test
//    public void show_작성자() {
//        AnswerDto newQuestionDto = createAnswerDto(4);
//        String location = createResource(newQuestionDto);
//
//        QuestionDto insertedQuestionDto = getResource(location, basicAuthTemplate(defaultUser()), QuestionDto.class);
//        assertEquals(newQuestionDto, insertedQuestionDto);
//    }
//
//    @Test
//    public void show_다른사람() {
//        AnswerDto newQuestionDto = createAnswerDto(5);
//        String location = createResource(newQuestionDto);
//
//        QuestionDto insertedQuestionDto = getResource(location, basicAuthTemplate(otherUser), QuestionDto.class);
//        assertEquals(newQuestionDto, insertedQuestionDto);
//    }
//
//    @Test
//    public void update_작성자() throws Exception {
//        AnswerDto newQuestionDto = createAnswerDto(6);
//        String location = createResource(newQuestionDto);
//
//        QuestionDto updateQuestion = new QuestionDto(newQuestionDto.getId(), "update title", "update contents");
//        basicAuthTemplate().put(location, updateQuestion);
//
//        QuestionDto dbQuestion = getResource(location, template(), QuestionDto.class);
//        assertEquals(dbQuestion, updateQuestion);
//    }
//
//    @Test
//    public void update_다른사람() throws Exception {
//        AnswerDto newQuestionDto = createAnswerDto(7);
//        String location = createResource(newQuestionDto);
//
//        QuestionDto updateQuestion = new QuestionDto(newQuestionDto.getId(), "update title", "update contents");
//        basicAuthTemplate(otherUser).put(location, updateQuestion);
//
//        QuestionDto dbQuestion = getResource(location, template(), QuestionDto.class);
//        assertEquals(dbQuestion, newQuestionDto);
//    }
//
//    @Test
//    public void delete_작성자() {
//        AnswerDto newQuestionDto = createAnswerDto(8);
//        String location = createResource(newQuestionDto);
//
//        basicAuthTemplate().delete(location);
//
//        QuestionDto dbQuestion = getResource(location, template(), QuestionDto.class);
//        assertNull(dbQuestion);
//    }
//
//    @Test
//    public void delete_다른사람() {
//        AnswerDto newQuestionDto = createAnswerDto(9);
//        String location = createResource(newQuestionDto);
//
//        basicAuthTemplate(otherUser).delete(location);
//
//        QuestionDto dbQuestion = getResource(location, template(), QuestionDto.class);
//        assertNotNull(dbQuestion);
//    }
//
//    @Test
//    public void delete_게스트() {
//        AnswerDto newQuestionDto = createAnswerDto(10);
//        String location = createResource(newQuestionDto);
//
//        template().delete(location);
//        QuestionDto dbQuestion = getResource(location, template(), QuestionDto.class);
//        assertNotNull(dbQuestion);
//    }

    private AnswerDto createAnswerDto(long answerId) {
        return new AnswerDto(answerId, "test contents");
    }
}
