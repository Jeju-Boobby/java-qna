package codesquad.web;

import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private User otherUser;

    @Before
    public void setUp() throws Exception {
        otherUser = findByUserId("sanjigi");
    }

    @Test
    public void create_login() throws Exception {
        QuestionDto newQuestionDto = createQuestionDto(1);
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        String location = response.getHeaders().getLocation().getPath();

        QuestionDto insertedQuestionDto = template().getForObject(location, QuestionDto.class);
        assertEquals(newQuestionDto, insertedQuestionDto);
    }

    @Test
    public void create_not_login() throws Exception {
        QuestionDto newQuestionDto = createQuestionDto(2);
        ResponseEntity<String> response = template().postForEntity("/api/questions", newQuestionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void show_모든사람() {
        QuestionDto newQuestionDto = createQuestionDto(3);
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        String location = response.getHeaders().getLocation().getPath();

        //로그인 안했을 때
        QuestionDto insertedQuestionDto = template().getForObject(location, QuestionDto.class);
        assertEquals(newQuestionDto, insertedQuestionDto);

        //작성자 로그인 했을 때
        insertedQuestionDto = basicAuthTemplate().getForObject(location, QuestionDto.class);
        assertEquals(newQuestionDto, insertedQuestionDto);

        //다른사람 로그인 했을 때
        insertedQuestionDto = basicAuthTemplate(otherUser).getForObject(location, QuestionDto.class);
        assertEquals(newQuestionDto, insertedQuestionDto);
    }

//    @Test
//    public void update() throws Exception {
//        QuestionDto newUser = createQuestionDto("testuser3");
//        ResponseEntity<String> response = template().postForEntity("/api/users", newUser, String.class);
//        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
//        String location = response.getHeaders().getLocation().getPath();
//
//        User loginUser = findByUserId(newUser.getUserId());
//        UserDto updateUser = new UserDto(newUser.getUserId(), "password", "name2", "javajigi@slipp.net2");
//        basicAuthTemplate(loginUser).put(location, updateUser);
//
//        UserDto dbUser = basicAuthTemplate(findByUserId(newUser.getUserId())).getForObject(location, UserDto.class);
//        assertThat(dbUser, is(updateUser));
//    }
//
//    @Test
//    public void update_다른_사람() throws Exception {
//        QuestionDto newUser = createQuestionDto("testuser4");
//        ResponseEntity<String> response = template().postForEntity("/api/users", newUser, String.class);
//        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
//        String location = response.getHeaders().getLocation().getPath();
//
//        UserDto updateUser = new UserDto(newUser.getUserId(), "password", "name2", "javajigi@slipp.net2");
//        basicAuthTemplate(defaultUser()).put(location, updateUser);
//
//        UserDto dbUser = basicAuthTemplate(findByUserId(newUser.getUserId())).getForObject(location, UserDto.class);
//        assertThat(dbUser, is(newUser));
//    }

    private QuestionDto createQuestionDto(long questionId) {
        return new QuestionDto(questionId, "test title", "test contents");
    }
}
