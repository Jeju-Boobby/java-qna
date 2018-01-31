package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.service.QnaService;
import codesquad.service.UserService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;
import support.test.HtmlFormDataBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class QnaAcceptanceTest extends AcceptanceTest {
    private static final Logger logger = LoggerFactory.getLogger(QnaAcceptanceTest.class);

    @Autowired
    private QnaService qnaService;

    @Autowired
    private UserService userService;

    @Test
    public void createForm_no_login() {
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void createForm_login() {
        ResponseEntity<String> response = basicAuthTemplate().getForEntity("/questions/form", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        logger.debug("body: {}", response.getBody());

        // path 가져올때 NullPointException 발생.. 왜???
//        assertThat(response.getHeaders().getLocation().getPath(), is("/questions/form"));
    }

    @Test
    public void create_no_login() {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("title", "test")
                .addParameter("contents", "테스트중입니다.")
                .build();

        ResponseEntity<String> response = template().postForEntity("/questions", request, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void create_login() {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("title", "test")
                .addParameter("contents", "테스트중입니다.")
                .build();

        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/questions", request, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertThat(response.getHeaders().getLocation().getPath(), is("/"));
    }

    @Test
    public void read_one() {
        long id = 1;
        Question question = qnaService.findById(id);

        ResponseEntity<String> response = template().getForEntity(String.format("/questions/%d", id), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().contains(question.getTitle()), is(true));
    }

    @Test
    public void update_form_no_login() {
        long questionId = 1;

        ResponseEntity<String> response = template().getForEntity(String.format("/questions/%d/form", questionId), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void update_form_owner_login() {
        long questionId = 1;
        Question question = qnaService.findById(questionId);

        ResponseEntity<String> reponse = basicAuthTemplate()
                .getForEntity(String.format("/questions/%d/form", questionId), String.class);

        assertThat(reponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(reponse.getBody().contains(question.getContents()), is(true));

    }

    @Test
    public void update_form_not_owner_login() {
        long questionId = 1;
        long userId = 2;

        User user = userService.findOne(userId);

        ResponseEntity<String> reponse = basicAuthTemplate(user)
                .getForEntity(String.format("/questions/%d/form", questionId), String.class);

        assertThat(reponse.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
}