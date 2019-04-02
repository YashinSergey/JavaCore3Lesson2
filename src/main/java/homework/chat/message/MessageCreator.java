package ru.geekbrains.classes.lesson8.message;

import java.time.LocalDate;

public class MessageCreator {

    private String userFrom;
    private String userTo;
    private String message;
    private LocalDate date;

    public MessageCreator(String userFrom, String userTo, String message) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.message = message;
        this.date = LocalDate.now();
    }

    public String getUserFrom() {
        return userFrom;
    }

    public String getUserTo() {
        return userTo;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getDate() {
        return date;
    }
}
