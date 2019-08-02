package me.codeminions.zhizhi.bean;

import java.io.Serializable;

public class Answer implements Serializable {
    private String author;
    private String author_des;

    private String question;
    private String answer;

    private String questionId;
    private String answerId;

    private String praise;
    private String comment;

    public Answer(String author, String author_des, String question, String answer, String questionId, String answerId, String praise, String comment) {
        this(author, author_des, question, answer, questionId, answerId);
        this.praise = praise;
        this.comment = comment;
    }

    public Answer(String author, String author_des, String question, String answer, String questionId, String answerId) {
        this.author = author;
        this.author_des = author_des;
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

    public String getAuthor() {
        return author;
    }

    public String getAuthor_des() {
        return author_des;
    }

    public String getPraise() {
        return praise;
    }

    public String getComment() {
        return comment;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
