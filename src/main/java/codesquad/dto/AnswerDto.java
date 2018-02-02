package codesquad.dto;

import codesquad.domain.Question;

import javax.validation.constraints.Size;

public class AnswerDto {
    private long id;
    private Question question;

    @Size(min = 5)
    private String contents;

    public AnswerDto() {

    }

    public AnswerDto(long answerId, Question question, String contents) {
        this.id = answerId;
        this. question = question;
        this.contents = contents;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
