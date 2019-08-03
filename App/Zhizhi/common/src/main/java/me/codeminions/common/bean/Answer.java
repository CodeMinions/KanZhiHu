package me.codeminions.common.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Answer implements Serializable {

    @Expose
    private User author;

    @Expose
    private String question;
    @Expose
    private String answer;

    @Expose
    private String html;

    @Expose
    private String questionId;
    @Expose
    private String answerId;

    @Expose
    private String praise;
    @Expose
    private String comment;

    public Answer(User author, String html) {
        this.author = author;
        this.html = html;
    }

    public Answer(String author, String author_des, String question, String answer, String questionId, String answerId, String praise, String comment) {
        this(author, author_des, question, answer, questionId, answerId);
        this.praise = praise;
        this.comment = comment;
    }

    public Answer(String author, String author_des, String question, String answer, String questionId, String answerId) {
        this.author = new User(author, author_des);
        this.question = question;
        this.answer = answer;
        this.answerId = answerId;
        this.questionId = questionId;
    }

    public Answer(String question, String answer, String questionId, String answerId) {
        this.question = question;
        this.answer = answer;
        this.answerId = answerId;
        this.questionId = questionId;
    }

    public Answer(String question, String answer){
        this.answer = answer;
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAnswerId() {
        return answerId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "author='" + author + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", questionId='" + questionId + '\'' +
                ", answerId='" + answerId + '\'' +
                ", praise='" + praise + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}

