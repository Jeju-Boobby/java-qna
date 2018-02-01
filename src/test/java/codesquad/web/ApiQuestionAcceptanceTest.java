package codesquad.web;

import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private User otherUser;

    @Before
    public void setUp() throws Exception {
        otherUser = findByUserId("sanjigi");
        super.setDefaultRequestUrl("/api/questions");
    }

    @Test
    public void create_login() throws Exception {
        QuestionDto newQuestionDto = createQuestionDto(1);
        String location = createResource(newQuestionDto);

        QuestionDto insertedQuestionDto = getResource(location, template(), QuestionDto.class);
        assertEquals(newQuestionDto, insertedQuestionDto);
    }

    @Test
    public void create_not_login() throws Exception {
        QuestionDto newQuestionDto = createQuestionDto(2);
        ResponseEntity<String> response = template().postForEntity(super.getDefaultRequestUrl(), newQuestionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void show_게스트() {
        QuestionDto newQuestionDto = createQuestionDto(3);
        String location = createResource(newQuestionDto);

        QuestionDto insertedQuestionDto = getResource(location, template(), QuestionDto.class);
        assertEquals(newQuestionDto, insertedQuestionDto);
    }

    @Test
    public void show_작성자() {
        QuestionDto newQuestionDto = createQuestionDto(4);
        String location = createResource(newQuestionDto);

        QuestionDto insertedQuestionDto = getResource(location, basicAuthTemplate(defaultUser()), QuestionDto.class);
        assertEquals(newQuestionDto, insertedQuestionDto);
    }

    @Test
    public void show_다른사람() {
        QuestionDto newQuestionDto = createQuestionDto(5);
        String location = createResource(newQuestionDto);

        QuestionDto insertedQuestionDto = getResource(location, basicAuthTemplate(otherUser), QuestionDto.class);
        assertEquals(newQuestionDto, insertedQuestionDto);
    }

    @Test
    public void update_작성자() throws Exception {
        QuestionDto newQuestionDto = createQuestionDto(6);
        String location = createResource(newQuestionDto);

        QuestionDto updateQuestion = new QuestionDto(newQuestionDto.getId(), "update title", "update contents");
        basicAuthTemplate().put(location, updateQuestion);

        QuestionDto dbQuestion = getResource(location, template(), QuestionDto.class);
        assertEquals(dbQuestion, updateQuestion);
    }

    @Test
    public void update_다른사람() throws Exception {
        QuestionDto newQuestionDto = createQuestionDto(7);
        String location = createResource(newQuestionDto);

        QuestionDto updateQuestion = new QuestionDto(newQuestionDto.getId(), "update title", "update contents");
        basicAuthTemplate(otherUser).put(location, updateQuestion);

        QuestionDto dbQuestion = getResource(location, template(), QuestionDto.class);
        assertEquals(dbQuestion, newQuestionDto);
    }

    @Test
    public void delete_작성자() {
        QuestionDto newQuestionDto = createQuestionDto(8);
        String location = createResource(newQuestionDto);

        basicAuthTemplate().delete(location);

        QuestionDto dbQuestion = getResource(location, template(), QuestionDto.class);
        assertNull(dbQuestion);
    }

    @Test
    public void delete_다른사람() {
        QuestionDto newQuestionDto = createQuestionDto(9);
        String location = createResource(newQuestionDto);

        basicAuthTemplate(otherUser).delete(location);

        QuestionDto dbQuestion = getResource(location, template(), QuestionDto.class);
        assertNotNull(dbQuestion);
    }

    @Test
    public void delete_게스트() {
        QuestionDto newQuestionDto = createQuestionDto(10);
        String location = createResource(newQuestionDto);

        template().delete(location);
        QuestionDto dbQuestion = getResource(location, template(), QuestionDto.class);
        assertNotNull(dbQuestion);
    }

    private QuestionDto createQuestionDto(long questionId) {
        return new QuestionDto(questionId, "test title", "test contents");
    }
}
