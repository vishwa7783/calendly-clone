package com.io.mountblue.calendlyclone.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Builder(toBuilder = true)
@ToString
public class EmailDto {
    private String from;
    private String to;
    private List<String> toList;
    private String message;
    private String subject;

    public EmailDto() {
    }

    public EmailDto(String from, String to, List<String> toList, String message, String subject) {
        this.from = from;
        this.to = to;
        this.toList = toList;
        this.message = message;
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<String> getToList() {
        return toList;
    }

    public void setToList(List<String> toList) {
        this.toList = toList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}